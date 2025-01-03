package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundInformationStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundPaymentStatus;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "refund_informations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundInformation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refund_information_sequences")
    @GenericGenerator(
            name = "refund_information_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.REFUND_INFORMATION),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "refund_information_sequences")
            })
    private String id;

    private String subId;

    private String supplierId;

    private String supplierName;

    private BigDecimal totalRefundedQuantity;

    private BigDecimal totalRefundedValue;

    private BigDecimal totalRefundedCost;

    private BigDecimal totalRefundedTax;

    private BigDecimal totalRefundedDiscount;

    private BigDecimal refundAmount;

    private String reason;

    @Enumerated(EnumType.STRING)
    private RefundInformationStatus status;

    @Enumerated(EnumType.STRING)
    private RefundPaymentStatus refundPaymentStatus;

    private LocalDateTime refundReceivedDate;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "grn_id")
    private GRN grn;

    @OneToMany(mappedBy = "refundInformation")
    private List<RefundInformationDetail> refundInformationDetails;

    @Override
    protected void customPrePersist() {
        if ((subId == null || subId.isEmpty()) && id != null) {
            subId = id;
        }
    }

    public void calculator() {
        if (totalRefundedTax == null) {
            totalRefundedTax = BigDecimal.ZERO;
        }
        if (totalRefundedDiscount == null) {
            totalRefundedDiscount = BigDecimal.ZERO;
        }
        if (totalRefundedCost == null) {
            totalRefundedCost = BigDecimal.ZERO;
        }

        this.totalRefundedQuantity = this.refundInformationDetails.stream()
                .map(RefundInformationDetail::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalRefundedValue = this.refundInformationDetails.stream()
                .map(RefundInformationDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.refundAmount = this.totalRefundedValue.subtract(this.totalRefundedTax).subtract(this.totalRefundedDiscount);
    }

    public void calculatorRefundPaymentStatus(BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(refundAmount) == 0) {
            this.refundPaymentStatus = RefundPaymentStatus.FULL;
        } else if (paymentAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.refundPaymentStatus = RefundPaymentStatus.NOT_REFUNDED;
        } else {
            this.refundPaymentStatus = RefundPaymentStatus.PARTIAL;
        }
    }
}
