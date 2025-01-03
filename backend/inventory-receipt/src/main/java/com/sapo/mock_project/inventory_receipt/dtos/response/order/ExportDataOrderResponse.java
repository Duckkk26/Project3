package com.sapo.mock_project.inventory_receipt.dtos.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportDataOrderResponse extends BaseResponse {
    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    @JsonProperty("supplier_name")
    private String supplierName;

    @JsonProperty("total_quantity")
    private BigDecimal totalQuantity;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;
}
