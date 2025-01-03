package com.sapo.mock_project.inventory_receipt.dtos.request.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO dùng để nhận yêu cầu lấy danh sách các danh mục.
 * Được sử dụng để gửi các tiêu chí lọc danh mục đến server.
 */
@Data
@Schema(description = "Thông tin yêu cầu để lấy danh sách danh mục")
public class GetListCategoryRequest {

    /**
     * Từ khóa dùng để tìm kiếm danh mục.
     *
     * @return Từ khóa tìm kiếm danh mục.
     */
    @JsonProperty("keyword")
    @Schema(description = "Từ khóa để tìm kiếm danh mục", example = "Điện thoại")
    private String keyword;
}
