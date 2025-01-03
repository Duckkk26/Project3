package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu lấy danh sách danh mục phiếu thu/chi.
 * <p>
 * Lớp này chứa các thông tin cần thiết để lọc và tìm kiếm danh mục phiếu thu/chi, bao gồm từ khóa tìm kiếm và loại phiếu thu/chi.
 * </p>
 */
@Data
@Schema(description = "Yêu cầu lấy danh sách danh mục phiếu thu/chi")
public class GetListTransactionCategoryRequest {

    /**
     * Từ khóa tìm kiếm để lọc danh mục phiếu thu/chi.
     * <p>
     * Đây là chuỗi ký tự dùng để tìm kiếm trong tên và mô tả của danh mục phiếu thu/chi.
     * </p>
     */
    @JsonProperty("keyword")
    @Schema(description = "Từ khóa tìm kiếm để lọc danh mục phiếu thu/chi", example = "Danh mục")
    private String keyword;

    /**
     * Loại phiếu thu/chi để lọc danh mục phiếu thu/chi.
     * <p>
     * Đây là loại phiếu thu/chi được định nghĩa bởi {@link TransactionType}, dùng để phân loại danh mục phiếu thu/chi.
     * </p>
     */
    @JsonProperty("type")
    @Schema(description = "Loại phiếu thu/chi để lọc danh mục phiếu thu/chi", example = "PURCHASE")
    private TransactionType type;
}
