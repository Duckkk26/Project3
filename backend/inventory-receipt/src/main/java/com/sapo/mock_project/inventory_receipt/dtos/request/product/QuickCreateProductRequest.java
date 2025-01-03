package com.sapo.mock_project.inventory_receipt.dtos.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuickCreateProductRequest {
    @NotBlank(message = MessageValidateKeys.PRODUCT_NAME_NOT_BLANK)
    @JsonProperty("name")
    private String name;

    @JsonProperty("cost_price")
    private BigDecimal costPrice;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("unit")
    private String unit;
}
