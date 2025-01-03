package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import com.sapo.mock_project.inventory_receipt.validator.ValidPassword;
import com.sapo.mock_project.inventory_receipt.validator.ValidPhone;
import lombok.Data;

@Data
public class AdminCreateStaffRequest {
    @JsonProperty("full_name")
    private String fullName;

    @ValidPhone
    @JsonProperty("phone")
    private String phone;

    @ValidPassword
    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private RoleEnum role;
}
