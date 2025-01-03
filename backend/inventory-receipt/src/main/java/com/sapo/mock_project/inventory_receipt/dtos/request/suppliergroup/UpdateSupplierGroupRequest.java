package com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu cập nhật nhóm nhà cung cấp.
 */
@Data
public class UpdateSupplierGroupRequest {

    /**
     * ID của nhóm nhà cung cấp cần cập nhật.
     *
     * @schema description = "ID nhóm nhà cung cấp cần cập nhật"
     */
    @Schema(description = "ID nhóm nhà cung cấp cần cập nhật", example = "SUPG002")
    @JsonProperty("sub_id")
    private String subId;

    /**
     * Tên mới của nhóm nhà cung cấp.
     *
     * @schema description = "Tên mới của nhóm nhà cung cấp"
     */
    @Schema(description = "Tên mới của nhóm nhà cung cấp", example = "Nhóm nhà cung cấp B")
    @JsonProperty("name")
    private String name;

    /**
     * Ghi chú mới về nhóm nhà cung cấp.
     *
     * @schema description = "Ghi chú mới về nhóm nhà cung cấp"
     */
    @Schema(description = "Ghi chú mới về nhóm nhà cung cấp", example = "Ghi chú đã được cập nhật cho nhóm này")
    @JsonProperty("note")
    private String note;
}
