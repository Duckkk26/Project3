package com.sapo.mock_project.inventory_receipt.dtos.request.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO yêu cầu để lấy danh sách nhà cung cấp dựa trên các tiêu chí lọc.
 */
@Data
public class GetListSupplierRequest {

    /**
     * Từ khóa tìm kiếm nhà cung cấp.
     * Có thể là tên, số điện thoại, hoặc ID nhà cung cấp.
     */
    @Schema(description = "Từ khóa tìm kiếm nhà cung cấp", example = "Công ty ABC", required = false)
    @JsonProperty("keyword")
    private String keyword;

    /**
     * Trạng thái của nhà cung cấp để lọc.
     * Có thể là ACTIVE, INACTIVE, hoặc DELETED.
     */
    @Schema(description = "Trạng thái của nhà cung cấp để lọc", example = "ACTIVE", required = false)
    @JsonProperty("statuses")
    private List<SupplierStatus> statuses;

    /**
     * ID của nhóm nhà cung cấp để lọc.
     */
    @Schema(description = "ID của nhóm nhà cung cấp để lọc", example = "GROUP001", required = false)
    @JsonProperty("supplier_group_ids")
    private List<String> supplierGroupIds;

    /**
     * Ngày tạo của nhà cung cấp để lọc.
     */
    @Schema(description = "Ngày tạo của nhà cung cấp để lọc", example = "2024-09-15", required = false)
    @JsonProperty("created_date_from")
    private LocalDate createdDateFrom;

    /**
     * Ngày tạo của nhà cung cấp để lọc.
     */
    @Schema(description = "Ngày tạo của nhà cung cấp để lọc", example = "2024-09-15", required = false)
    @JsonProperty("created_date_to")
    private LocalDate createdDateTo;

    /**
     * Các thẻ hoặc tag liên quan đến nhà cung cấp để lọc.
     */
    @Schema(description = "Các thẻ hoặc tag liên quan đến nhà cung cấp để lọc", example = "Nhà cung cấp chính, Ưu tiên cao", required = false)
    @JsonProperty("tags")
    private String tags;
}
