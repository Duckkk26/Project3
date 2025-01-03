package com.sapo.mock_project.inventory_receipt.dtos.response.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO phản hồi chi tiết thương hiệu.
 */
@Data
public class BrandDetailResponse {

    @Schema(description = "ID của thương hiệu", example = "BRND001")
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @Schema(description = "Tên thương hiệu", example = "Thương hiệu A")
    @JsonProperty("name")
    private String name;
}
