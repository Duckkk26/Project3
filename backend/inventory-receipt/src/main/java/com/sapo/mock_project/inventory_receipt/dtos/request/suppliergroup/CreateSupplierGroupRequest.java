package com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu tạo nhóm nhà cung cấp.
 */
@Data
public class CreateSupplierGroupRequest {

    /**
     * ID của nhóm nhà cung cấp.
     *
     * @schema description = "ID nhóm nhà cung cấp"
     */
    @Schema(description = "ID nhóm nhà cung cấp", example = "SUPG001")
    @JsonProperty("sub_id")
    private String subId;

    /**
     * Tên của nhóm nhà cung cấp.
     *
     * @schema description = "Tên nhóm nhà cung cấp"
     */
    @Schema(description = "Tên nhóm nhà cung cấp", example = "Nhóm nhà cung cấp A")
    @JsonProperty("name")
    private String name;

    /**
     * Ghi chú về nhóm nhà cung cấp.
     *
     * @schema description = "Ghi chú về nhóm nhà cung cấp"
     */
    @Schema(description = "Ghi chú về nhóm nhà cung cấp", example = "Ghi chú về nhóm nhà cung cấp này")
    @JsonProperty("note")
    private String note;
}
