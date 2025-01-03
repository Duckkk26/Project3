package com.sapo.mock_project.inventory_receipt.dtos.response.grn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.constants.enums.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GRNGetListResponse extends BaseResponse {
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonProperty("sub_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subId;

    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GRNStatus status;

    @JsonProperty("receive_status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GRNReceiveStatus receiveStatus;

    @JsonProperty("payment_status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GRNPaymentStatus paymentStatus;

    @JsonProperty("return_status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GRNReturnStatus returnStatus;

    @JsonProperty("refund_status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GRNRefundStatus refundStatus;

    @JsonProperty("received_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime receivedAt;

    @JsonProperty("expected_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime expectedAt;

    @JsonProperty("cancelled_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime cancelledAt;

    @JsonProperty("payment_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime paymentAt;

    @JsonProperty("total_received_quantity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalReceivedQuantity;

    @JsonProperty("total_value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalValue;

    @JsonProperty("supplier_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierName;

    @JsonProperty("supplier_sub_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierSubId;

    @JsonProperty("supplier_phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierPhone;

    @JsonProperty("supplier_email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierEmail;

    @JsonProperty("user_created_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userCreatedName;

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

    @JsonProperty("order_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderSubId;
}
