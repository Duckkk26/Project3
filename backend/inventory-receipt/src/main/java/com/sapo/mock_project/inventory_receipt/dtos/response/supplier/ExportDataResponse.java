package com.sapo.mock_project.inventory_receipt.dtos.response.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

@Data
public class ExportDataResponse extends BaseResponse {
    @JsonProperty("name")
    private String name;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("sub_id_group")
    private String subIdGroup;

    @JsonProperty("name_group")
    private String nameGroup;
}
