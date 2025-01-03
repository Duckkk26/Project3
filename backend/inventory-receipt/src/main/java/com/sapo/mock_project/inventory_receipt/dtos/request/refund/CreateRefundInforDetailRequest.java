package com.sapo.mock_project.inventory_receipt.dtos.request.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRefundInforDetailRequest {
    @NotNull(message = MessageValidateKeys.PRODUCT_NOT_NULL)
    @JsonProperty("product_id")
    private String productId;

   // @ValidNumber(message = MessageValidateKeys.REFUND_QUANTITY_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.REFUND_QUANTITY_NOT_NEGATIVE)
    @JsonProperty("quantity")
    private BigDecimal quantity;

   // @ValidNumber(message = MessageValidateKeys.REFUND_PRICE_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.REFUND_PRICE_NOT_NEGATIVE)
    @JsonProperty("refunded_price")
    private BigDecimal refundedPrice;

   // @ValidNumber(message = MessageValidateKeys.REFUND_AMOUNT_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.REFUND_AMOUNT_NOT_NEGATIVE)
    @JsonProperty("amount")
    private BigDecimal amount;
}
