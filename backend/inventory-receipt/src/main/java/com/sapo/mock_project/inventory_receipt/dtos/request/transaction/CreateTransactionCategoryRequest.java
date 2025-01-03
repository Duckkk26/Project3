package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu tạo một danh mục phiếu thu/chi mới.
 * <p>
 * Lớp này chứa các thông tin cần thiết để tạo một danh mục phiếu thu/chi mới bao gồm ID, tên, mô tả, và loại phiếu thu/chi.
 * </p>
 */
@Data
@Schema(description = "Yêu cầu tạo một danh mục phiếu thu/chi mới")
public class CreateTransactionCategoryRequest {

    /**
     * ID của danh mục phiếu thu/chi.
     * <p>
     * Đây là mã định danh duy nhất của danh mục phiếu thu/chi, có thể được sử dụng để nhận diện danh mục trong cơ sở dữ liệu.
     * </p>
     */
    @JsonProperty("sub_id")
    @Schema(description = "ID của danh mục phiếu thu/chi", example = "12345")
    private String subId;

    /**
     * Tên của danh mục phiếu thu/chi.
     * <p>
     * Đây là tên của danh mục phiếu thu/chi được sử dụng để nhận diện và phân loại danh mục.
     * </p>
     */
    @JsonProperty("name")
    @Schema(description = "Tên của danh mục phiếu thu/chi", example = "Danh mục 1")
    private String name;

    /**
     * Mô tả của danh mục phiếu thu/chi.
     * <p>
     * Đây là mô tả chi tiết về danh mục phiếu thu/chi, cung cấp thêm thông tin về danh mục.
     * </p>
     */
    @JsonProperty("description")
    @Schema(description = "Mô tả của danh mục phiếu thu/chi", example = "Mô tả chi tiết về danh mục 1")
    private String description;

    /**
     * Loại phiếu thu/chi của danh mục.
     * <p>
     * Đây là loại phiếu thu/chi mà danh mục thuộc về, được định nghĩa bởi {@link TransactionType}.
     * </p>
     */
    @JsonProperty("type")
    @Schema(description = "Loại phiếu thu/chi của danh mục", example = "PURCHASE")
    private TransactionType type;
}
