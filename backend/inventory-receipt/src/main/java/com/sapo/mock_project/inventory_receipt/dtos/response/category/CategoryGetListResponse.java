package com.sapo.mock_project.inventory_receipt.dtos.response.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

@Data
public class CategoryGetListResponse extends BaseResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("name")
    private String name;
}
