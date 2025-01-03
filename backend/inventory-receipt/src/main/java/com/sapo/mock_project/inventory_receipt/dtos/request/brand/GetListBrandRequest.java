package com.sapo.mock_project.inventory_receipt.dtos.request.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetListBrandRequest {
    @JsonProperty("name")
    private String name;
}
