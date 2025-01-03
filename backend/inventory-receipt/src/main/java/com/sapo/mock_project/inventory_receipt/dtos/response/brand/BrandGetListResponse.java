package com.sapo.mock_project.inventory_receipt.dtos.response.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BrandGetListResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("name")
    private String name;
}
