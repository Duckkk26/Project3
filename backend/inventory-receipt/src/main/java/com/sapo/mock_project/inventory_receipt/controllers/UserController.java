package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.user.UserService;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller xử lý các yêu cầu HTTP liên quan đến chức năng của người dùng (đăng ký, đăng nhập, đổi mật khẩu, v.v.).
 *
 * <p>Sử dụng {@link UserService} để thực hiện các chức năng nghiệp vụ.</p>
 */
@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * API tạo tài khoản mới.
     *
     * <p>Đăng ký tài khoản mới với thông tin được cung cấp trong {@link RegisterAccountRequest}.</p>
     *
     * @param request đối tượng chứa thông tin đăng ký tài khoản của người dùng.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với dữ liệu hoặc thông báo kết quả.
     */
    @Operation(
            summary = "Tạo tài khoản mới",
            description = "Đăng ký tài khoản mới với thông tin được cung cấp.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tạo tài khoản thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
                    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)))
            }
    )
    @PostMapping("/register.json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<ResponseObject<Object>> createAccount(@Valid @RequestBody RegisterAccountRequest request) {
        StringUtils.trimAllStringFields(request);

        return userService.createAccount(request);
    }

    /**
     * API đăng nhập tài khoản.
     *
     * <p>Đăng nhập với thông tin được cung cấp trong {@link LoginAccountRequest} và trả về thông tin người dùng.</p>
     *
     * @param request đối tượng chứa thông tin đăng nhập của người dùng.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với dữ liệu hoặc thông báo kết quả.
     */
    @Operation(
            summary = "Đăng nhập tài khoản",
            description = "Đăng nhập với thông tin đăng nhập và trả về thông tin người dùng.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Đăng nhập thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
                    @ApiResponse(responseCode = "400", description = "Thông tin đăng nhập không hợp lệ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)))
            }
    )
    @PostMapping("/login.json")
    public ResponseEntity<ResponseObject<Object>> login(@Valid @RequestBody LoginAccountRequest request) {
        StringUtils.trimAllStringFields(request);

        return userService.loginAccount(request);
    }

    /**
     * API lấy chi tiết thông tin người dùng từ token.
     *
     * <p>Lấy thông tin chi tiết của người dùng dựa trên token hiện tại.</p>
     *
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với thông tin chi tiết người dùng.
     */
    @Operation(
            summary = "Lấy chi tiết thông tin người dùng",
            description = "Lấy thông tin chi tiết của người dùng dựa trên token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
                    @ApiResponse(responseCode = "401", description = "Token không hợp lệ hoặc hết hạn", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)))
            }
    )
    @GetMapping("/detail.json")
    public ResponseEntity<ResponseObject<Object>> getUserDetailByToken() {
        return userService.getUserDetailByToken();
    }

    /**
     * API làm mới (refresh) token.
     *
     * <p>Làm mới token với thông tin refresh token được cung cấp trong {@link RefreshTokenRequest}.</p>
     *
     * @param request đối tượng chứa refresh token của người dùng.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với token mới hoặc thông báo kết quả.
     */
    @Operation(
            summary = "Làm mới token",
            description = "Làm mới token với thông tin refresh token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Làm mới token thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
                    @ApiResponse(responseCode = "401", description = "Refresh token không hợp lệ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)))
            }
    )
    @PostMapping("/refresh-token.json")
    public ResponseEntity<ResponseObject<Object>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        StringUtils.trimAllStringFields(request);

        return userService.refreshToken(request);
    }

    /**
     * API thay đổi mật khẩu.
     *
     * <p>Thay đổi mật khẩu của người dùng với thông tin được cung cấp trong {@link ChangePasswordRequest}.</p>
     *
     * @param request đối tượng chứa thông tin yêu cầu đổi mật khẩu.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với thông báo kết quả.
     */
    @Operation(
            summary = "Thay đổi mật khẩu",
            description = "Thay đổi mật khẩu của người dùng.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thay đổi mật khẩu thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
                    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
            }
    )
    @PatchMapping("/change-password.json")
    public ResponseEntity<ResponseObject<Object>> changePasswordAccount(@Valid @RequestBody ChangePasswordRequest request) {
        StringUtils.trimAllStringFields(request);

        return userService.changePasswordAccount(request);
    }

    @PostMapping("/admin-create.json")
    public ResponseEntity<ResponseObject<Object>> adminCreateAccount(@Valid @RequestBody AdminCreateStaffRequest request) {
        StringUtils.trimAllStringFields(request);

        return userService.adminCreateAccount(request);
    }

    @PostMapping("/list.json")
    public ResponseEntity<ResponseObject<Object>> getListAccount(@Valid @RequestBody GetListAccountRequest request,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        return userService.getListAccount(request, page, size);
    }

    @PatchMapping("/admin-update-account.json/{id}")
    public ResponseEntity<ResponseObject<Object>> adminUpdateAccount(@PathVariable String id,
                                                                  @Valid @RequestBody AdminUpdateAccountRequest request) {
        StringUtils.trimAllStringFields(request);

        return userService.adminUpdateAccount(id, request);
    }

    @DeleteMapping("/delete-account.json/{id}")
    public ResponseEntity<ResponseObject<Object>> deleteAccount(@PathVariable String id) {
        return userService.deleteAccount(id);
    }

    @GetMapping("/list-name.json")
    public ResponseEntity<ResponseObject<Object>> getListName(@RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        return userService.getListName(page, size);
    }

    @GetMapping("/confirm-email/{userId}")
    public ModelAndView confirmEmail(@PathVariable String userId, @RequestParam String verifyCode) {
        return userService.confirmEmail(userId, verifyCode);
    }

    @GetMapping("/dashboard.json")
    public ResponseEntity<ResponseObject<Object>> getDashboard() {
        return userService.getDashboard();
    }
}
