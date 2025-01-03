package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class UpdateGRNRequest {
    /**
     * Các thẻ hoặc tag liên quan đến phiếu nhập kho.
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("tags")
    @Schema(description = "Các thẻ hoặc tag liên quan đến phiếu nhập kho", example = "Nhập hàng, Ưu tiên", required = false)
    private String tags;

    /**
     * Ghi chú về phiếu nhập kho.
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("note")
    @Schema(description = "Ghi chú về phiếu nhập kho", example = "Nhập hàng lần đầu từ nhà cung cấp mới", required = false)
    private String note;


    /**
     * Số tiền giảm giá cho phiếu nhập kho.
     * Đây là thông tin bắt buộc.
     */
    @JsonProperty("discount")
    @Schema(description = "Số tiền giảm giá cho phiếu nhập kho", example = "500000.00", required = false)
    private BigDecimal discount;



    /**
     * Ngày dự kiến giao hàng.
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("expected_delivery_at")
    @Schema(description = "Ngày dự kiến giao hàng", example = "2023-09-15", required = false)
    private String expectedDeliveryAt;

    /**
     * Danh sách các sản phẩm liên quan đến phiếu nhập kho.
     * Đây là thông tin bắt buộc.
     */
    @JsonProperty("products")
    @Schema(description = "Danh sách các sản phẩm", required = true)
    private List<GRNProduct> products;

    /**
     * Danh sách chi phí nhập khẩu liên quan đến phiếu nhập kho.
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("import_cost")
    @Schema(description = "Danh sách chi phí nhập khẩu", required = false)
    private List<GRNImportCost> importCosts;

    /**
     * Danh sách phương thức thanh toán liên quan đến phiếu nhập kho.
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("payment_method")
    @Schema(description = "Danh sách phương thức thanh toán", required = false)
    private List<GRNPaymentMethod> paymentMethods;

    /**
     * ID của đơn hàng liên quan đến phiếu nhập kho.
     * Đây là thông tin tùy chọn.
     */




}
