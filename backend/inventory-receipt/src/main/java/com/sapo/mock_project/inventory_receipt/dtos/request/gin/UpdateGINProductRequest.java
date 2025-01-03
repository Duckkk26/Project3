package com.sapo.mock_project.inventory_receipt.dtos.request.gin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateGINProductRequest {
   // @ValidNumber(message = MessageValidateKeys.GIN_PRODUCT_ACTUAL_STOCK_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.GIN_PRODUCT_ACTUAL_STOCK_NOT_NEGATIVE)
    @JsonProperty("actual_stock")
    private BigDecimal actualStock;

   // @ValidNumber(message = MessageValidateKeys.GIN_PRODUCT_DISCREPANCY_QUANTITY_NOT_NUMBER)
    @JsonProperty("discrepancy_quantity")
    private BigDecimal discrepancyQuantity;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("note")
    private String note;

    @JsonProperty("unit")
    private String unit;

    @NotNull(message = MessageValidateKeys.PRODUCT_NOT_NULL)
    @JsonProperty("product_id")
    private String productId;
}
