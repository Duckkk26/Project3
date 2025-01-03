package com.sapo.mock_project.inventory_receipt.dtos.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GetListProductManageRequest {
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("category_ids")
    private List<String> categoryIds;

    @JsonProperty("created_date_from")
    private LocalDate createdDateFrom;

    @JsonProperty("created_date_to")
    private LocalDate createdDateTo;

    @JsonProperty("brand_ids")
    private List<String> brandIds;

    @JsonProperty("statues")
    private List<ProductStatus> statuses;
}
