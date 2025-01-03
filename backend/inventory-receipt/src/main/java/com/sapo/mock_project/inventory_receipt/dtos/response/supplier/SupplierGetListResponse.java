package com.sapo.mock_project.inventory_receipt.dtos.response.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO phản hồi cho danh sách các nhà cung cấp.
 * Kế thừa từ {@link BaseResponse} để cung cấp các thông tin cơ bản về phản hồi.
 */
@Data
public class SupplierGetListResponse extends BaseResponse {

    /**
     * ID của nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "ID của nhà cung cấp", example = "SUP12345")
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonProperty("sub_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subId;

    /**
     * Tên của nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Tên của nhà cung cấp", example = "Công ty ABC")
    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    /**
     * Địa chỉ email của nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Địa chỉ email của nhà cung cấp", example = "info@abc.com")
    @JsonProperty("email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    /**
     * Số điện thoại của nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Số điện thoại của nhà cung cấp", example = "0123456789")
    @JsonProperty("phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    /**
     * Địa chỉ của nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Địa chỉ của nhà cung cấp", example = "123 Đường ABC, Quận 1, TP.HCM")
    @JsonProperty("address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;

    /**
     * Các thẻ hoặc tag liên quan đến nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Các thẻ hoặc tag liên quan đến nhà cung cấp", example = "Nhà cung cấp chính, Ưu tiên cao")
    @JsonProperty("tags")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tags;

    /**
     * Ghi chú về nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Ghi chú về nhà cung cấp", example = "Nhà cung cấp lâu năm, uy tín")
    @JsonProperty("note")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String note;

    /**
     * Số nợ hiện tại của nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Số nợ hiện tại của nhà cung cấp", example = "500000")
    @JsonProperty("current_debt")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal currentDebt;

    /**
     * Trạng thái của nhà cung cấp.
     * Xem {@link SupplierStatus} để biết các trạng thái có thể có.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Trạng thái của nhà cung cấp", example = "ACTIVE")
    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SupplierStatus status;

    /**
     * Tổng số tiền hoàn lại cho nhà cung cấp.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Tổng số tiền hoàn lại cho nhà cung cấp", example = "200000")
    @JsonProperty("total_refund")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalRefund;

    /**
     * Tên của nhóm nhà cung cấp mà nhà cung cấp này thuộc về.
     * Trường này có thể là null nếu không có dữ liệu.
     */
    @Schema(description = "Tên của nhóm nhà cung cấp mà nhà cung cấp này thuộc về", example = "Nhóm A")
    @JsonProperty("supplier_group_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplierGroupName;
}
