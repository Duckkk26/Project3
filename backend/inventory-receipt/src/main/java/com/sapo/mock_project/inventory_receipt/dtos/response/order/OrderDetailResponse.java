package com.sapo.mock_project.inventory_receipt.dtos.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponse extends BaseResponse {
    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("image")
    private ProductImage productImage;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("imported_quantity")
    private BigDecimal importedQuantity;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("discount")
    private BigDecimal discount;

    @JsonProperty("tax")
    private BigDecimal tax;
}
