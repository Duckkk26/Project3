package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionGRNRequest {
    @JsonProperty("grn_id")
    private String grnId;

    @JsonProperty("payment_method")
    private TransactionMethod paymentMethod;

    @JsonProperty("amount")
    private BigDecimal amount;
}
