package com.sapo.mock_project.inventory_receipt.dtos.response.suppliergroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierGroupStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO phản hồi chi tiết nhóm nhà cung cấp.
 */
@Data
public class SupplierDetailResponse extends BaseResponse {

    /**
     * ID của nhóm nhà cung cấp.
     *
     * @schema description = "ID của nhóm nhà cung cấp"
     */
    @Schema(description = "ID của nhóm nhà cung cấp", example = "SUPG003")
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    /**
     * Tên của nhóm nhà cung cấp.
     *
     * @schema description = "Tên của nhóm nhà cung cấp"
     */
    @Schema(description = "Tên của nhóm nhà cung cấp", example = "Nhóm nhà cung cấp C")
    @JsonProperty("name")
    private String name;

    /**
     * Ghi chú về nhóm nhà cung cấp.
     *
     * @schema description = "Ghi chú về nhóm nhà cung cấp"
     */
    @Schema(description = "Ghi chú về nhóm nhà cung cấp", example = "Ghi chú chi tiết về nhóm này")
    @JsonProperty("note")
    private String note;

    /**
     * Trạng thái của nhóm nhà cung cấp.
     *
     * @schema description = "Trạng thái của nhóm nhà cung cấp"
     * @schema example = "ACTIVE"
     */
    @Schema(description = "Trạng thái của nhóm nhà cung cấp", example = "ACTIVE")
    @JsonProperty("status")
    private SupplierGroupStatus status;
}
