package com.sapo.mock_project.inventory_receipt.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Lớp đại diện cho đối tượng phản hồi API chung, bao gồm mã trạng thái, thông điệp và dữ liệu.
 *
 * @param <T> kiểu dữ liệu của dữ liệu phản hồi.
 */
@Builder
@Data
public class ResponseObject<T> {

    /**
     * Mã trạng thái HTTP của phản hồi.
     */
    @Schema(description = "Mã trạng thái HTTP của phản hồi", example = "200")
    @JsonProperty("status_code")
    private int statusCode;

    /**
     * Trạng thái của phản hồi, thường là "OK", "ERROR", v.v.
     */
    @Schema(description = "Trạng thái của phản hồi", example = "OK")
    @JsonProperty("status")
    private String status;

    /**
     * Thông điệp liên quan đến phản hồi, như thông báo lỗi hoặc thông báo thành công.
     */
    @Schema(description = "Thông điệp liên quan đến phản hồi", example = "Yêu cầu thành công")
    @JsonProperty("message")
    private String message;

    /**
     * Dữ liệu trả về từ API, có thể là bất kỳ kiểu dữ liệu nào.
     * Nếu không có dữ liệu, thuộc tính này sẽ không có trong phản hồi.
     */
    @Schema(description = "Dữ liệu trả về từ API", example = "{ \"userId\": 123, \"fullName\": \"Nguyễn Văn A\" }")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    private T data;
}
