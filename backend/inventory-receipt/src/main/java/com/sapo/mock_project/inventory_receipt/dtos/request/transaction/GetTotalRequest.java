package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GetTotalRequest {
    @JsonProperty("mode")
    private TransactionType mode;

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("user_created_ids")
    private List<String> userCreatedIds;

    @JsonProperty("payment_methods")
    private List<TransactionMethod> paymentMethods;

    @JsonProperty("date_from")
    private LocalDate dateFrom;

    @JsonProperty("date_to")
    private LocalDate dateTo;

    @JsonProperty("date_type")
    private String dateType;
}
