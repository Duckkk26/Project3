package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateGRNProductRequest {
    @NotNull(message = MessageValidateKeys.PRODUCT_NOT_NULL)
    @JsonProperty("product_id")
    private String productId;

   // @ValidNumber(message = MessageValidateKeys.GRN_PRODUCT_QUANTITY_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.GRN_PRODUCT_QUANTITY_NOT_NEGATIVE)
    @JsonProperty("quantity")
    private BigDecimal quantity;

   // @ValidNumber(message = MessageValidateKeys.GRN_PRODUCT_DISCOUNT_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.GRN_PRODUCT_DISCOUNT_NOT_NEGATIVE)
    @JsonProperty("discount")
    private BigDecimal discount;

   // @ValidNumber(message = MessageValidateKeys.GRN_PRODUCT_TAX_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.GRN_PRODUCT_TAX_NOT_NEGATIVE)
    @JsonProperty("tax")
    private BigDecimal tax;

   // @ValidNumber(message = MessageValidateKeys.GRN_PRODUCT_PRICE_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.GRN_PRODUCT_PRICE_NOT_NEGATIVE)
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("note")
    private String note;
}
