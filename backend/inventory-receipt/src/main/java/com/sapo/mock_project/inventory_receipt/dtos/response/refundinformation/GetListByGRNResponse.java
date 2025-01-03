package com.sapo.mock_project.inventory_receipt.dtos.response.refundinformation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundInformationStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundPaymentStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class GetListByGRNResponse extends BaseResponse {
    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("supplier_name")
    private String supplierName;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("transaction")
    private Map<String, Object> transation;

    @JsonProperty("total_refunded_quantity")
    private BigDecimal totalRefundedQuantity;

    @JsonProperty("total_refunded_value")
    private BigDecimal totalRefundedValue;

    @JsonProperty("status")
    private RefundInformationStatus status;

    @JsonProperty("refund_payment_status")
    private RefundPaymentStatus refundPaymentStatus;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    @JsonProperty("refund_details")
    private List<RefundDetailResponse> refundDetails;
}
