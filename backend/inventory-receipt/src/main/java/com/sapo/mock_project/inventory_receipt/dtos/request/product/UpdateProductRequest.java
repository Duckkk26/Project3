package com.sapo.mock_project.inventory_receipt.dtos.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductRequest {
    @JsonProperty("sub_id")
    private String subId;

    @NotBlank(message = MessageValidateKeys.PRODUCT_NAME_NOT_BLANK)
    @JsonProperty("name")
    private String name;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("cost_price")
    private BigDecimal costPrice;

    @JsonProperty("wholesale_price")
    private BigDecimal wholesalePrice;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    @JsonProperty("images")
    private List<ProductImage> images;

    @JsonProperty("types")
    private List<ProductType> types;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("brand_id")
    private String brandId;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private ProductStatus status;
}
