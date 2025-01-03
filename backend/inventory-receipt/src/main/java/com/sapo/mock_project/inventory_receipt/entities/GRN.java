package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.*;
import com.sapo.mock_project.inventory_receipt.converts.GRNHistoryConverter;
import com.sapo.mock_project.inventory_receipt.converts.GRNImportCostConverter;
import com.sapo.mock_project.inventory_receipt.converts.GRNPaymentMethodConverter;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNHistory;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "grns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class GRN extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grn_sequences")
    @GenericGenerator(
            name = "grn_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.GRN),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "grn_sequences")
            })
    private String id;

    private String subId;

    @Enumerated(EnumType.STRING)
    private GRNStatus status;

    @Enumerated(EnumType.STRING)
    private GRNReceiveStatus receivedStatus;

    private LocalDateTime expectedDeliveryAt;

    private LocalDateTime receivedAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime paymentAt;

    private BigDecimal totalReceivedQuantity;

    private BigDecimal discount;

    private BigDecimal taxAmount;

    private BigDecimal totalValue;

    private BigDecimal totalPaid;

    @Enumerated(EnumType.STRING)
    private GRNPaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private ReturnStatus returnStatus;

    @Enumerated(EnumType.STRING)
    private GRNRefundStatus refundStatus;

    private String note;

    private String tags;

    @Convert(converter = GRNImportCostConverter.class)
    @Column(name = "import_costs")
    private List<GRNImportCost> importCosts;

    @Convert(converter = GRNPaymentMethodConverter.class)
    @Column(name = "payment_methods")
    private List<GRNPaymentMethod> paymentMethods;

    @Convert(converter = GRNHistoryConverter.class)
    @Column(name = "histories")
    private List<GRNHistory> histories;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "user_completed_id")
    private User userCompleted;

    @ManyToOne
    @JoinColumn(name = "user_cancelled_id")
    private User userCancelled;

    @ManyToOne
    @JoinColumn(name = "user_imported_id")
    private User userImported;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "grn")
    private List<RefundInformation> refundInformations;

    @OneToMany(mappedBy = "grn")
    private List<GRNProduct> grnProducts;

    public void calculatorValue() {
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        if (totalPaid == null) {
            totalPaid = BigDecimal.ZERO;
        }

        BigDecimal totalValueProduct = grnProducts.stream()
                .map(detail -> (detail.getPrice().multiply(detail.getQuantity())).subtract(detail.getDiscount()).add(detail.getTax()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalValue = totalValueProduct.add(taxAmount).subtract(discount);
        totalReceivedQuantity = grnProducts.stream()
                .map(GRNProduct::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (importCosts != null && !importCosts.isEmpty()) {
            BigDecimal totalImportCost = importCosts.stream()
                    .map(GRNImportCost::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            totalValue = totalValue.add(totalImportCost);
        }

        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            BigDecimal totalPaid = paymentMethods.stream()
                    .map(GRNPaymentMethod::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            this.totalPaid = totalPaid;
        }
    }

    @Override
    protected void customPrePersist() {
        if ((subId == null || subId.isEmpty()) && id != null) {
            subId = id;
        }
    }

    public void calculatorRefundStatus() {
        if (refundInformations != null && !refundInformations.isEmpty()) {
            BigDecimal totalRefund = refundInformations.stream()
                    .map(RefundInformation::getTotalRefundedQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalRefund.compareTo(totalReceivedQuantity) >= 0) {
                refundStatus = GRNRefundStatus.FULL;
            } else {
                refundStatus = GRNRefundStatus.PARTIAL;
            }
        } else {
            refundStatus = GRNRefundStatus.NOT_REFUNDED;
        }
    }

}
