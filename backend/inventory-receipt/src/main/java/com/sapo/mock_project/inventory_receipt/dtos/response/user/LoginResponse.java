package com.sapo.mock_project.inventory_receipt.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import lombok.Builder;
import lombok.Data;

/**
 * DTO dùng để phản hồi khi đăng nhập thành công.
 * Chứa thông tin về người dùng và các token.
 */
@Data
@Builder
public class LoginResponse {

    /**
     * ID của người dùng.
     * Trường này chứa ID duy nhất của người dùng trong hệ thống.
     */
    @Schema(description = "ID của người dùng", example = "12345")
    @JsonProperty("user_id")
    private String userId;

    /**
     * Họ và tên của người dùng.
     * Trường này chứa tên đầy đủ của người dùng.
     */
    @Schema(description = "Họ và tên của người dùng", example = "Nguyễn Văn A")
    @JsonProperty("full_name")
    private String fullName;

    /**
     * Số điện thoại của người dùng.
     * Trường này chứa số điện thoại đã đăng ký của người dùng.
     */
    @Schema(description = "Số điện thoại của người dùng", example = "0123456789")
    @JsonProperty("phone")
    private String phone;

    /**
     * Token JWT của người dùng.
     * Trường này chứa token JWT dùng để xác thực các yêu cầu của người dùng.
     */
    @Schema(description = "Token JWT của người dùng", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    @JsonProperty("token")
    private String token;

    /**
     * Token làm mới JWT của người dùng.
     * Trường này chứa token dùng để làm mới token JWT khi cần thiết.
     */
    @Schema(description = "Token làm mới JWT của người dùng", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * Loại token (ví dụ: Bearer).
     * Trường này chứa loại token mà API sử dụng.
     */
    @Schema(description = "Loại token (ví dụ: Bearer)", example = "Bearer")
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * Vai trò của người dùng.
     * Trường này chứa thông tin về vai trò của người dùng trong hệ thống.
     */
    @Schema(description = "Vai trò của người dùng", example = "USER")
    @JsonProperty("role")
    private RoleEnum role;
}
