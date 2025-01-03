package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO dùng để yêu cầu làm mới token của người dùng.
 * Chứa thông tin cần thiết để thực hiện việc làm mới token.
 */
@Data
public class RefreshTokenRequest {

    /**
     * Token làm mới của người dùng.
     * Trường này là bắt buộc và cần phải hợp lệ để có thể làm mới token.
     */
    @Schema(description = "Token làm mới của người dùng", required = true, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    @JsonProperty("refresh_token")
    private String refreshToken;
}
