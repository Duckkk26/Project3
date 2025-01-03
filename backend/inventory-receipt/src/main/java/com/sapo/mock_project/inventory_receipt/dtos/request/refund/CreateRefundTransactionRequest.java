package com.sapo.mock_project.inventory_receipt.dtos.request.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRefundTransactionRequest {
   // @ValidNumber(message = MessageValidateKeys.TRANSACTION_AMOUNT_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.TRANSACTION_AMOUNT_NOT_NEGATIVE)
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("payment_method")
    private TransactionMethod paymentMethod;
}
