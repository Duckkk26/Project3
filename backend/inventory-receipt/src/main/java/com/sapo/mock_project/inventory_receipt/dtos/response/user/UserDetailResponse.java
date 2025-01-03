package com.sapo.mock_project.inventory_receipt.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO dùng để phản hồi thông tin chi tiết của người dùng.
 * Kế thừa từ {@link BaseResponse} để thêm thông tin cơ bản của phản hồi.
 */
@Data
public class UserDetailResponse extends BaseResponse {

    /**
     * ID của người dùng.
     * Trường này chứa ID duy nhất của người dùng trong hệ thống.
     */
    @JsonProperty("id")
    @Schema(description = "ID của người dùng", example = "12345")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    /**
     * Họ và tên của người dùng.
     * Trường này chứa tên đầy đủ của người dùng.
     */
    @JsonProperty("full_name")
    @Schema(description = "Họ và tên của người dùng", example = "Nguyễn Văn A")
    private String fullName;

    /**
     * Số điện thoại của người dùng.
     * Trường này chứa số điện thoại của người dùng.
     */
    @JsonProperty("phone")
    @Schema(description = "Số điện thoại của người dùng", example = "0123456789")
    private String phone;

    /**
     * Địa chỉ email của người dùng.
     * Trường này chứa địa chỉ email của người dùng.
     */
    @JsonProperty("email")
    @Schema(description = "Địa chỉ email của người dùng", example = "nguyenvana@example.com")
    private String email;

    /**
     * Giới tính của người dùng.
     * Trường này chứa giới tính của người dùng dưới dạng số nguyên.
     * Ví dụ: 1 - Nam, 2 - Nữ.
     */
    @JsonProperty("gender")
    @Schema(description = "Giới tính của người dùng", example = "1")
    private short gender;

    /**
     * Đường dẫn tới avatar của người dùng.
     * Trường này chứa URL của ảnh đại diện người dùng.
     */
    @JsonProperty("avatar")
    @Schema(description = "Đường dẫn tới avatar của người dùng", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * Trạng thái hoạt động của người dùng.
     * Trường này cho biết người dùng có đang hoạt động hay không.
     */
    @JsonProperty("is_active")
    @Schema(description = "Trạng thái hoạt động của người dùng", example = "true")
    private boolean isActive;

    /**
     * Vai trò của người dùng.
     * Trường này chứa thông tin về vai trò của người dùng trong hệ thống.
     */
    @JsonProperty("role")
    @Schema(description = "Vai trò của người dùng", example = "USER")
    private RoleEnum role;
}
