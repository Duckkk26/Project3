package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequences")
    @GenericGenerator(
            name = "order_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.ORDER),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "order_sequences")
            })
    private String id;

    private String subId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String note;

    private String tags;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal totalPrice;

    private LocalDateTime expectedAt;

    private LocalDateTime completedAt;

    private LocalDateTime endedAt;

    private LocalDateTime cancelledAt;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "user_completed_id")
    private User userCompleted;

    @ManyToOne
    @JoinColumn(name = "user_cancelled_id")
    private User userCancelled;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order")
    private List<GRN> grns;

    @Override
    protected void customPrePersist() {
        if ((subId == null || subId.isEmpty()) && id != null) {
            subId = id;
        }
    }

    public void initializeOrder() {
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }

        if (tax == null) {
            tax = BigDecimal.ZERO;
        }

        if (totalPrice == null) {
            totalPrice = BigDecimal.ZERO;
        }

        for (OrderDetail detail : orderDetails) {
            detail.setImportedQuantity(BigDecimal.ZERO);
        }
    }

    public void calculateTotalPrice() {
        BigDecimal totalDetailsPrice = orderDetails.stream()
                .map(detail -> (detail.getPrice().subtract(detail.getDiscount()).add(detail.getTax())).multiply(detail.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalPrice = totalDetailsPrice.add(this.tax).subtract(this.discount);
    }
}
