package com.sapo.mock_project.inventory_receipt.dtos.response.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetTotalTransactionResponse extends BaseResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("type")
    private TransactionType type;

    @JsonProperty("payment_method")
    private TransactionMethod paymentMethod;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("reference_code")
    private String referenceCode;

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note")
    private String note;

    @JsonProperty("recipient_group")
    private String recipientGroup;

    @JsonProperty("recipient_id")
    private String recipientId;

    @JsonProperty("recipient_name")
    private String recipientName;
}
