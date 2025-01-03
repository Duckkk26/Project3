package com.sapo.mock_project.inventory_receipt.dtos.request.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO yêu cầu tạo mới thương hiệu.
 */
@Data
public class CreateBrandRequest {
    @NotBlank(message = MessageValidateKeys.BRAND_NAME_NOT_BLANK)
    @Schema(description = "Tên thương hiệu", example = "Thương hiệu A")
    @JsonProperty("name")
    private String name;
}
