package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNReceiveStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GetListGRNRequest {
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("statuses")
    private List<GRNStatus> statuses;

    @JsonProperty("received_statuses")
    private List<GRNReceiveStatus> receivedStatuses;

    @JsonProperty("supplier_ids")
    private List<String> supplierIds;

    @JsonProperty("start_created_at")
    private LocalDate startCreatedAt;

    @JsonProperty("end_created_at")
    private LocalDate endCreatedAt;

    @JsonProperty("start_expected_at")
    private LocalDate startExpectedAt;

    @JsonProperty("end_expected_at")
    private LocalDate endExpectedAt;

    @JsonProperty("product_ids")
    private List<String> productIds;

    @JsonProperty("user_created_ids")
    private List<String> userCreatedIds;

    @JsonProperty("user_completed_ids")
    private List<String> userCompletedIds;

    @JsonProperty("user_cancelled_ids")
    private List<String> userCancelledIds;
}
