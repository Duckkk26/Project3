package com.sapo.mock_project.inventory_receipt.dtos.request.gin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.entities.GINProduct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGINRequest {
    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("note")
    private String note;

    @JsonProperty("tags")
    private String tags;

    @NotNull(message = MessageValidateKeys.GIN_USER_INSPECTION_NOT_NULL)
    @JsonProperty("user_inspection_id")
    private String userInspectionId;

    @NotEmpty(message = MessageValidateKeys.GIN_PRODUCTS_NOT_EMPTY)
    @JsonProperty("products")
    List<CreateGINProductRequest> products;

    @JsonProperty("is_balance")
    private boolean isBalance;
}
