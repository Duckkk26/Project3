package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import com.sapo.mock_project.inventory_receipt.validator.ValidPassword;
import com.sapo.mock_project.inventory_receipt.validator.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;

/**
 * Lớp DTO dùng để đăng ký tài khoản người dùng mới.
 * Chứa các thông tin cần thiết để thực hiện việc đăng ký người dùng.
 */
@Data
@Builder
@Schema(description = "Thông tin đăng ký tài khoản người dùng mới")
public class RegisterAccountRequest {

    /**
     * Tên đầy đủ của người dùng.
     * Trường này là bắt buộc và nên là một chuỗi tên hợp lệ.
     */
    @JsonProperty("full_name")
    @Schema(description = "Tên đầy đủ của người dùng", example = "Nguyễn Văn A", required = true)
    private String fullName;

    /**
     * Số điện thoại của người dùng.
     * Trường này là bắt buộc và nên là một chuỗi số điện thoại hợp lệ.
     */
    @ValidPhone
    @JsonProperty("phone")
    @Schema(description = "Số điện thoại của người dùng", example = "0912345678", required = true)
    private String phone;

    /**
     * Mật khẩu cho tài khoản người dùng.
     * Trường này là bắt buộc và nên là một chuỗi mật khẩu bảo mật.
     */
    @ValidPassword
    @JsonProperty("password")
    @Schema(description = "Mật khẩu cho tài khoản người dùng", example = "password123", required = true)
    private String password;

    /**
     * Vai trò được gán cho người dùng.
     * Trường này là bắt buộc và nên là một trong các vai trò đã định nghĩa.
     */
    @JsonProperty("role")
    @Schema(description = "Vai trò được gán cho người dùng", example = "USER", required = true)
    private RoleEnum role;

    @JsonProperty("email")
    private String email;
}
