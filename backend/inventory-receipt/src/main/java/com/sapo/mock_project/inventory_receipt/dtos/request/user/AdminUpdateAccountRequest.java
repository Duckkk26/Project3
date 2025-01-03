package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import lombok.Data;

@Data
public class AdminUpdateAccountRequest {
    @JsonProperty("role")
    private RoleEnum role;

    @JsonProperty("is_active")
    private boolean isActive;
}
