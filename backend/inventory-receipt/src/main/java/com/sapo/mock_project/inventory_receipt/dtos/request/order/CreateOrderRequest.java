package com.sapo.mock_project.inventory_receipt.dtos.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrderRequest {
    @NotNull(message = MessageValidateKeys.SUPPLIER_NOT_NULL)
    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("expected_at")
    private LocalDate expectedAt;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note")
    private String note;

//   // @ValidNumber(message = MessageValidateKeys.ORDER_DISCOUNT_NOT_NUMBER)
    @PositiveOrZero(message = MessageValidateKeys.ORDER_DISCOUNT_NOT_NEGATIVE)
    @JsonProperty("discount")
    private BigDecimal discount;

    @NotEmpty(message = MessageValidateKeys.ORDER_PRODUCTS_NOT_EMPTY)
    @JsonProperty("products")
    private List<CreateOrderDetailRequest> products;
}
