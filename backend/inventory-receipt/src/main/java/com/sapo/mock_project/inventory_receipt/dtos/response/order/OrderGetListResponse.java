package com.sapo.mock_project.inventory_receipt.dtos.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderGetListResponse extends BaseResponse {
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonProperty("sub_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subId;

    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderStatus status;

    @JsonProperty("supplier_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierName;

    @JsonProperty("user_created_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userCreatedName;

    @JsonProperty("quantity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalQuantity;

    @JsonProperty("price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalPrice;

    @JsonProperty("supplier_sub_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierSubId;

    @JsonProperty("supplier_phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierPhone;

    @JsonProperty("supplier_email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierEmail;

    @JsonProperty("user_completed_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userCompletedName;

    @JsonProperty("user_cancelled_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userCancelledName;

    @JsonProperty("note")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String note;

    @JsonProperty("tags")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tags;

    @JsonProperty("expected_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime expectedAt;
}
