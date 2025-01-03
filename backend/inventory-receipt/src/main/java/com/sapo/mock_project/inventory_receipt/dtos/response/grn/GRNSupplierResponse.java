package com.sapo.mock_project.inventory_receipt.dtos.response.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNPaymentStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNReceiveStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GRNSupplierResponse extends BaseResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("status")
    private GRNStatus status;

    @JsonProperty("received_status")
    private GRNReceiveStatus receivedStatus;

    @JsonProperty("payment_status")
    private GRNPaymentStatus paymentStatus;

    @JsonProperty("total_value")
    private BigDecimal totalValue;
}
