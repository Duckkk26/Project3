package com.sapo.mock_project.inventory_receipt.dtos.request.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetListDebtSupplierRequest {
    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("created_date_from")
    private LocalDate createdDateFrom;

    @JsonProperty("created_date_to")
    private LocalDate createdDateTo;
}