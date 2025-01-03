package com.sapo.mock_project.inventory_receipt.dtos.request.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateRefundInforRequest {
    @NotNull(message = MessageValidateKeys.GRN_NOT_NULL)
    @JsonProperty("grn_id")
    private String grnId;

   // @ValidNumber(message = MessageValidateKeys.REFUND_TOTAL_COST_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.REFUND_TOTAL_COST_NOT_NEGATIVE)
    @JsonProperty("total_cost")
    private BigDecimal totalCost;

   // @ValidNumber(message = MessageValidateKeys.REFUND_TOTAL_TAX_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.REFUND_TOTAL_TAX_NOT_NEGATIVE)
    @JsonProperty("total_tax")
    private BigDecimal totalTax;

   // @ValidNumber(message = MessageValidateKeys.REFUND_TOTAL_DISCOUNT_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.REFUND_TOTAL_DISCOUNT_NOT_NEGATIVE)
    @JsonProperty("total_discount")
    private BigDecimal totalDiscount;

    @JsonProperty("reason")
    private String reason;

    @NotEmpty(message = MessageValidateKeys.REFUND_DETAIL_NOT_EMPTY)
    @JsonProperty("refund_detail")
    private List<CreateRefundInforDetailRequest> refundDetail;

    @JsonProperty("transaction")
    private CreateRefundTransactionRequest transaction;
}
