package com.sapo.mock_project.inventory_receipt.dtos.response;

import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Lớp tiện ích để tạo các phản hồi {@link ResponseEntity} với các mã trạng thái và thông điệp khác nhau.
 */
public class ResponseUtil {
    private ResponseUtil() {
    }

    /**
     * Tạo phản hồi thành công với mã trạng thái HTTP 200, thông điệp và dữ liệu.
     *
     * @param message Thông điệp trả về.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 200 và dữ liệu.
     */
    public static ResponseEntity<ResponseObject<Object>> success200Response(String message) {
        return ResponseEntity
                .ok()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.getReasonPhrase())
                        .message(message)
                        .build());
    }

    /**
     * Tạo phản hồi thành công với mã trạng thái HTTP 200, thông điệp và dữ liệu.
     *
     * @param message Thông điệp trả về.
     * @param data    Dữ liệu trả về.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 200 và dữ liệu.
     */
    public static ResponseEntity<ResponseObject<Object>> success200Response(String message, Object data) {
        return ResponseEntity
                .ok()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.getReasonPhrase())
                        .message(message)
                        .data(data)
                        .build());
    }

    /**
     * Tạo phản hồi thành công với mã trạng thái HTTP 201 và thông điệp.
     *
     * @param message Thông điệp trả về.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 201.
     */
    public static ResponseEntity<ResponseObject<Object>> success201Response(String message) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED.getReasonPhrase())
                        .message(message)
                        .build());
    }

    /**
     * Tạo phản hồi thành công với mã trạng thái HTTP 201 và thông điệp.
     *
     * @param message Thông điệp trả về.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 201.
     */
    public static ResponseEntity<ResponseObject<Object>> success201Response(String message, Object data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED.getReasonPhrase())
                        .message(message)
                        .data(data)
                        .build());
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái HTTP 400 và thông điệp lỗi xác thực.
     *
     * @param message Thông điệp lỗi xác thực.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 400.
     */
    public static ResponseEntity<ResponseObject<Object>> errorValidationResponse(String message) {
        return ResponseEntity
                .badRequest()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(MessageValidateKeys.ERROR_VALIDATION)
                        .message(message)
                        .build());
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái HTTP 400 và thông điệp lỗi.
     *
     * @param message Thông điệp lỗi.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 400.
     */
    public static ResponseEntity<ResponseObject<Object>> error400Response(String message) {
        return ResponseEntity
                .badRequest()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(message)
                        .build());
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái HTTP 401 và thông điệp lỗi.
     *
     * @param message Thông điệp lỗi.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 401.
     */
    public static ResponseEntity<ResponseObject<Object>> error401Response(String message) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .message(message)
                        .build());
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái HTTP 404 và thông điệp lỗi.
     *
     * @param message Thông điệp lỗi.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 404.
     */
    public static ResponseEntity<ResponseObject<Object>> error404Response(String message) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(message)
                        .build());
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái HTTP 500 và thông điệp lỗi.
     *
     * @param message Thông điệp lỗi.
     * @return Đối tượng {@link ResponseEntity} chứa {@link ResponseObject} với mã trạng thái 500.
     */
    public static ResponseEntity<ResponseObject<Object>> error500Response(String message) {
        return ResponseEntity
                .internalServerError()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message(message)
                        .build());
    }
}
