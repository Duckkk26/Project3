package com.sapo.mock_project.inventory_receipt.dtos.response.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO phản hồi chi tiết thông tin của một nhà cung cấp.
 * Kế thừa từ {@link BaseResponse} để cung cấp các thông tin cơ bản về phản hồi.
 */
@Data
public class SupplierDetail extends BaseResponse {

    /**
     * ID của nhà cung cấp.
     */
    @Schema(description = "ID của nhà cung cấp", example = "SUP12345")
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    /**
     * Tên của nhà cung cấp.
     */
    @Schema(description = "Tên của nhà cung cấp", example = "Công ty ABC")
    @JsonProperty("name")
    private String name;

    /**
     * Số điện thoại của nhà cung cấp.
     */
    @Schema(description = "Số điện thoại của nhà cung cấp", example = "0123456789")
    @JsonProperty("phone")
    private String phone;

    /**
     * Địa chỉ email của nhà cung cấp.
     */
    @Schema(description = "Địa chỉ email của nhà cung cấp", example = "info@abc.com")
    @JsonProperty("email")
    private String email;

    /**
     * Địa chỉ của nhà cung cấp.
     */
    @Schema(description = "Địa chỉ của nhà cung cấp", example = "123 Đường ABC, Quận 1, TP.HCM")
    @JsonProperty("address")
    private String address;

    /**
     * Các thẻ hoặc tag liên quan đến nhà cung cấp.
     */
    @Schema(description = "Các thẻ hoặc tag liên quan đến nhà cung cấp", example = "Nhà cung cấp chính, Ưu tiên cao")
    @JsonProperty("tags")
    private String tags;

    /**
     * Ghi chú về nhà cung cấp.
     */
    @Schema(description = "Ghi chú về nhà cung cấp", example = "Nhà cung cấp lâu năm, uy tín")
    @JsonProperty("note")
    private String note;

    /**
     * Trạng thái của nhà cung cấp.
     * Xem {@link SupplierStatus} để biết các trạng thái có thể có.
     */
    @Schema(description = "Trạng thái của nhà cung cấp", example = "ACTIVE")
    @JsonProperty("status")
    private SupplierStatus status;

    /**
     * Số nợ hiện tại của nhà cung cấp.
     */
    @Schema(description = "Số nợ hiện tại của nhà cung cấp", example = "500000")
    @JsonProperty("current_debt")
    private BigDecimal currentDebt;

    /**
     * Tên của nhóm nhà cung cấp mà nhà cung cấp này thuộc về.
     */
    @Schema(description = "Tên của nhóm nhà cung cấp mà nhà cung cấp này thuộc về", example = "Nhóm A")
    @JsonProperty("supplier_group_name")
    private String supplierGroupName;
}
