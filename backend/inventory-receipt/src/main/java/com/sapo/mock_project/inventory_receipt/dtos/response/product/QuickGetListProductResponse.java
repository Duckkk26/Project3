package com.sapo.mock_project.inventory_receipt.dtos.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuickGetListProductResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private List<ProductImage> images;

    @JsonProperty("types")
    private List<ProductType> types;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("cost_price")
    private BigDecimal costPrice;

    @JsonProperty("unit")
    private String unit;
}
