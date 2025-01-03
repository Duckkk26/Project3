package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GetListAccountRequest {
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("roles")
    private List<RoleEnum> roles;

    @JsonProperty("start_created_date")
    private LocalDate startCreatedDate;

    @JsonProperty("end_created_date")
    private LocalDate endCreatedDate;

    @JsonProperty("active_status")
    private List<Boolean> activeStatuses;
}
