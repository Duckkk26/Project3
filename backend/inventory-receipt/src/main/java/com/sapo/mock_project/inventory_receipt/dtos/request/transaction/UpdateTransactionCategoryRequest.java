package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu cập nhật danh mục phiếu thu/chi.
 * <p>
 * Lớp này chứa các thông tin cần thiết để cập nhật một danh mục phiếu thu/chi hiện có,
 * bao gồm tên và mô tả của danh mục phiếu thu/chi.
 * </p>
 */
@Data
@Schema(description = "Yêu cầu cập nhật danh mục phiếu thu/chi")
public class UpdateTransactionCategoryRequest {

    /**
     * Tên mới của danh mục phiếu thu/chi.
     * <p>
     * Đây là tên mới được cập nhật cho danh mục phiếu thu/chi. Tên này sẽ thay thế tên hiện tại của danh mục.
     * </p>
     */
    @JsonProperty("name")
    @Schema(description = "Tên mới của danh mục phiếu thu/chi", example = "Danh mục mới")
    private String name;

    /**
     * Mô tả mới của danh mục phiếu thu/chi.
     * <p>
     * Đây là mô tả mới được cập nhật cho danh mục phiếu thu/chi, cung cấp thông tin chi tiết hơn về danh mục.
     * </p>
     */
    @JsonProperty("description")
    @Schema(description = "Mô tả mới của danh mục phiếu thu/chi", example = "Mô tả chi tiết về danh mục mới")
    private String description;
}
