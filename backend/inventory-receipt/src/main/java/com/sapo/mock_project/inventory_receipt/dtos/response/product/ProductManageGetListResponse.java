package com.sapo.mock_project.inventory_receipt.dtos.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductManageGetListResponse extends BaseResponse {
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonProperty("sub_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subId;

    @JsonProperty("images")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductImage> images;

    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonProperty("quantity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal quantity;

    @JsonProperty("cost_price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal costPrice;

    @JsonProperty("wholesale_price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal wholesalePrice;

    @JsonProperty("retail_price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal retailPrice;

    @JsonProperty("category_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String categoryName;

    @JsonProperty("brand_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String brandName;

    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductStatus status;

    @JsonProperty("incoming_quantity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal incomingQuantity;
}
