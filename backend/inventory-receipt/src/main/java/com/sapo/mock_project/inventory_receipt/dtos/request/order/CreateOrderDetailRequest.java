package com.sapo.mock_project.inventory_receipt.dtos.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderDetailRequest {
    @NotNull(message = MessageValidateKeys.PRODUCT_NOT_NULL)
    @JsonProperty("product_id")
    private String productId;

//   // @ValidNumber(message = MessageValidateKeys.ORDER_DETAIL_QUANTITY_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.ORDER_DETAIL_QUANTITY_NOT_NEGATIVE)
    @JsonProperty("quantity")
    private BigDecimal quantity;

//   // @ValidNumber(message = MessageValidateKeys.ORDER_DETAIL_PRICE_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.ORDER_DETAIL_PRICE_NOT_NEGATIVE)
    @JsonProperty("price")
    private BigDecimal price;

//   // @ValidNumber(message = MessageValidateKeys.ORDER_DETAIL_DISCOUNT_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.ORDER_DETAIL_DISCOUNT_NOT_NEGATIVE)
    @JsonProperty("discount")
    private BigDecimal discount;

//   // @ValidNumber(message = MessageValidateKeys.ORDER_DETAIL_TAX_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.ORDER_DETAIL_TAX_NOT_NEGATIVE)
    @JsonProperty("tax")
    private BigDecimal tax;

    @JsonProperty("unit")
    private String unit;
}
