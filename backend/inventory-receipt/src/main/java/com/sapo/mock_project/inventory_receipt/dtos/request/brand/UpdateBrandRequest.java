package com.sapo.mock_project.inventory_receipt.dtos.request.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO yêu cầu cập nhật thương hiệu.
 */
@Data
public class UpdateBrandRequest {
    @NotBlank(message = MessageValidateKeys.BRAND_NAME_NOT_BLANK)
    @Schema(description = "Tên mới của thương hiệu", example = "Thương hiệu B")
    @JsonProperty("name")
    private String name;
}
