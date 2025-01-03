package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Lớp DTO đại diện cho yêu cầu cập nhật phiếu thu/chi.
 * Chứa các thông tin cần thiết để cập nhật phiếu thu/chi như ID, ghi chú và thẻ.
 */
@Data
public class UpdateTransactionRequest {

    /**
     * Mã định danh của phiếu thu/chi cần cập nhật.
     */
    @JsonProperty("sub_id")
    @Schema(description = "Mã định danh của phiếu thu/chi cần cập nhật", example = "TX123456")
    private String subId;

    /**
     * Ghi chú cập nhật cho phiếu thu/chi.
     */
    @JsonProperty("note")
    @Schema(description = "Ghi chú cập nhật cho phiếu thu/chi", example = "Cập nhật thông tin phiếu thu/chi")
    private String note;

    /**
     * Các thẻ liên quan đến phiếu thu/chi.
     */
    @JsonProperty("tags")
    @Schema(description = "Các thẻ liên quan đến phiếu thu/chi", example = "thẻ1,thẻ2")
    private String tags;
}
