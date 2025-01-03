package com.sapo.mock_project.inventory_receipt.dtos.request.gin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GetListGINRequest {
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("statues")
    private List<GINStatus> statuses;

    @JsonProperty("created_date_from")
    private LocalDate createdDateFrom;

    @JsonProperty("created_date_to")
    private LocalDate createdDateTo;

    @JsonProperty("balanced_date_from")
    private LocalDate balancedDateFrom;

    @JsonProperty("balanced_date_to")
    private LocalDate balancedDateTo;

    @JsonProperty("user_created_ids")
    private List<String> userCreatedIds;

    @JsonProperty("user_balanced_ids")
    private List<String> userBalancedIds;

    @JsonProperty("user_inspection_ids")
    private List<String> userInspectionIds;
}
