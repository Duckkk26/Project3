package com.sapo.mock_project.inventory_receipt.dtos.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailResponse extends BaseResponse {
    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("brand_id")
    private String brandId;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("note")
    private String note;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("cost_price")
    private BigDecimal costPrice;

    @JsonProperty("wholesale_price")
    private BigDecimal wholesalePrice;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    @JsonProperty("status")
    private ProductStatus status;

    @JsonProperty("image")
    private ProductImage image;

    @JsonProperty("types")
    private List<ProductType> types;
}
