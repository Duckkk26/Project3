package com.sapo.mock_project.inventory_receipt.dtos.request.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidEmail;
import com.sapo.mock_project.inventory_receipt.validator.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO yêu cầu để tạo một nhà cung cấp mới.
 */
@Data
public class CreateSupplierRequest {

    /**
     * ID của nhà cung cấp.
     * Trường này có thể không cần thiết khi tạo mới và thường được hệ thống tự động sinh ra.
     */
    @Schema(description = "ID của nhà cung cấp", example = "SUP12345", required = false)
    @JsonProperty("sub_id")
    private String subId;

    /**
     * Tên của nhà cung cấp.
     * Đây là thông tin bắt buộc.
     */
    @Schema(description = "Tên của nhà cung cấp", example = "Công ty ABC", required = true)
    @JsonProperty("name")
    private String name;

    /**
     * Số điện thoại của nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @ValidPhone
    @Schema(description = "Số điện thoại của nhà cung cấp", example = "0123456789", required = false)
    @JsonProperty("phone")
    private String phone;

    /**
     * Địa chỉ email của nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @ValidEmail
    @Schema(description = "Địa chỉ email của nhà cung cấp", example = "info@abc.com", required = false)
    @JsonProperty("email")
    private String email;

    /**
     * Địa chỉ của nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @NotBlank(message = MessageValidateKeys.SUPPLIER_ADDRESS_NOT_BLANK)
    @Schema(description = "Địa chỉ của nhà cung cấp", example = "123 Đường ABC, Quận 1, TP.HCM", required = false)
    @JsonProperty("address")
    private String address;

    /**
     * ID của nhóm nhà cung cấp mà nhà cung cấp này thuộc về.
     * Đây là thông tin bắt buộc.
     */
    @Schema(description = "ID của nhóm nhà cung cấp mà nhà cung cấp này thuộc về", example = "GROUP001", required = true)
    @JsonProperty("supplier_group_id")
    private String supplierGroupId;

    /**
     * Các thẻ hoặc tag liên quan đến nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @Schema(description = "Các thẻ hoặc tag liên quan đến nhà cung cấp", example = "Nhà cung cấp chính, Ưu tiên cao", required = false)
    @JsonProperty("tags")
    private String tags;

    /**
     * Ghi chú về nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @Schema(description = "Ghi chú về nhà cung cấp", example = "Nhà cung cấp lâu năm, uy tín", required = false)
    @JsonProperty("note")
    private String note;
}
