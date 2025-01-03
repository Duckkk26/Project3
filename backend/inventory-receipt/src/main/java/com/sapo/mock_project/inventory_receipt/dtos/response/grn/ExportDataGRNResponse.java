package com.sapo.mock_project.inventory_receipt.dtos.response.grn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNReceiveStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExportDataGRNResponse extends BaseResponse {
    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("received_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime receivedAt;

    @JsonProperty("status")
    private GRNStatus status;

    @JsonProperty("received_status")
    private GRNReceiveStatus receivedStatus;

    @JsonProperty("supplier_name")
    private String supplierName;

    @JsonProperty("user_imported_name")
    private String userImportedName;

    @JsonProperty("total_value")
    private BigDecimal totalValue;
}
