package com.sapo.mock_project.inventory_receipt.dtos.response.gin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GINProductDetailResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("product_sub_id")
    private String productSubId;

    @JsonProperty("image")
    private ProductImage image;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("real_quantity")
    private BigDecimal realQuantity;

    @JsonProperty("actual_stock")
    private BigDecimal actualStock;

    @JsonProperty("discrepancy_quantity")
    private BigDecimal discrepancyQuantity;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("note")
    private String note;
}
