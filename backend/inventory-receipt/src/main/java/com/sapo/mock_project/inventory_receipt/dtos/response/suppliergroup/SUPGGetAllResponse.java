package com.sapo.mock_project.inventory_receipt.dtos.response.suppliergroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO phản hồi trả về danh sách nhóm nhà cung cấp.
 */
@Data
public class SUPGGetAllResponse extends BaseResponse {

    /**
     * ID của nhóm nhà cung cấp.
     *
     * @schema description = "ID của nhóm nhà cung cấp"
     */
    @Schema(description = "ID của nhóm nhà cung cấp", example = "SUPG001")
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    /**
     * Tên của nhóm nhà cung cấp.
     *
     * @schema description = "Tên của nhóm nhà cung cấp"
     */
    @Schema(description = "Tên của nhóm nhà cung cấp", example = "Nhóm nhà cung cấp A")
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

    /**
     * Tổng số nhà cung cấp trong nhóm.
     *
     * @schema description = "Tổng số nhà cung cấp trong nhóm"
     */
    @Schema(description = "Tổng số nhà cung cấp trong nhóm", example = "15")
    @JsonProperty("total_supplier")
    private int totalSupplier;
}
