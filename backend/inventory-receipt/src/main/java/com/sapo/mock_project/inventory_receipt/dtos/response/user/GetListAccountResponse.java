package com.sapo.mock_project.inventory_receipt.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

@Data
public class GetListAccountResponse extends BaseResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("role")
    private RoleEnum role;

    @JsonProperty("address")
    private String address;
}
