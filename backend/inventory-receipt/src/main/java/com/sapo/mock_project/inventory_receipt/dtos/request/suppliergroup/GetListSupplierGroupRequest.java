package com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierGroupStatus;
import lombok.Data;

@Data
public class GetListSupplierGroupRequest {
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("status")
    private SupplierGroupStatus status;
}
