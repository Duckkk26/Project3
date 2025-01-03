package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.validator.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO dùng để yêu cầu thay đổi mật khẩu của người dùng.
 * Chứa thông tin cần thiết để thay đổi mật khẩu tài khoản.
 */
@Data
public class ChangePasswordRequest {

    /**
     * Mật khẩu cũ của người dùng.
     * Trường này là bắt buộc và nên là mật khẩu hiện tại của người dùng.
     */
    @ValidPassword
    @Schema(description = "Mật khẩu cũ của người dùng", required = true, example = "oldPassword123")
    @JsonProperty("old_password")
    private String oldPassword;

    /**
     * Mật khẩu mới của người dùng.
     * Trường này là bắt buộc và nên là mật khẩu mới mà người dùng muốn đặt.
     */
    @ValidPassword
    @Schema(description = "Mật khẩu mới của người dùng", required = true, example = "newPassword456")
    @JsonProperty("new_password")
    private String newPassword;

    /**
     * Xác nhận mật khẩu mới của người dùng.
     * Trường này là bắt buộc và nên khớp với mật khẩu mới để xác nhận rằng người dùng đã nhập đúng.
     */
    @ValidPassword
    @Schema(description = "Xác nhận mật khẩu mới của người dùng", required = true, example = "newPassword456")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
