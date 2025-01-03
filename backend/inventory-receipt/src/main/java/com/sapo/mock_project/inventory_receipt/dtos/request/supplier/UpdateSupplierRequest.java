package com.sapo.mock_project.inventory_receipt.dtos.request.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu để cập nhật thông tin của một nhà cung cấp.
 */
@Data
public class UpdateSupplierRequest {

    /**
     * ID của nhà cung cấp cần cập nhật.
     * Trường này bắt buộc phải có để xác định nhà cung cấp cần sửa đổi.
     */
    @Schema(description = "ID của nhà cung cấp cần cập nhật", example = "SUP12345", required = true)
    @JsonProperty("sub_id")
    private String subId;

    /**
     * Tên của nhà cung cấp.
     * Có thể cập nhật để thay đổi tên của nhà cung cấp.
     */
    @Schema(description = "Tên của nhà cung cấp", example = "Công ty ABC", required = false)
    @JsonProperty("name")
    private String name;

    /**
     * Số điện thoại của nhà cung cấp.
     * Có thể cập nhật để thay đổi số điện thoại của nhà cung cấp.
     */
    @Schema(description = "Số điện thoại của nhà cung cấp", example = "0987654321", required = false)
    @JsonProperty("phone")
    private String phone;

    /**
     * Email của nhà cung cấp.
     * Có thể cập nhật để thay đổi email của nhà cung cấp.
     */
    @Schema(description = "Email của nhà cung cấp", example = "contact@abccompany.com", required = false)
    @JsonProperty("email")
    private String email;

    /**
     * Địa chỉ của nhà cung cấp.
     * Có thể cập nhật để thay đổi địa chỉ của nhà cung cấp.
     */
    @Schema(description = "Địa chỉ của nhà cung cấp", example = "123 Đường ABC, Thành phố XYZ", required = false)
    @JsonProperty("address")
    private String address;

    /**
     * ID của nhóm nhà cung cấp.
     * Có thể cập nhật để thay đổi nhóm của nhà cung cấp.
     */
    @Schema(description = "ID của nhóm nhà cung cấp", example = "GROUP001", required = false)
    @JsonProperty("supplier_group_id")
    private String supplierGroupId;

    /**
     * Các thẻ hoặc tag liên quan đến nhà cung cấp.
     * Có thể cập nhật để thay đổi các tag liên quan đến nhà cung cấp.
     */
    @Schema(description = "Các thẻ hoặc tag liên quan đến nhà cung cấp", example = "Nhà cung cấp chính, Ưu tiên cao", required = false)
    @JsonProperty("tags")
    private String tags;

    /**
     * Ghi chú về nhà cung cấp.
     * Có thể cập nhật để thay đổi ghi chú hoặc thêm thông tin mới.
     */
    @Schema(description = "Ghi chú về nhà cung cấp", example = "Nhà cung cấp đáng tin cậy", required = false)
    @JsonProperty("note")
    private String note;
}
