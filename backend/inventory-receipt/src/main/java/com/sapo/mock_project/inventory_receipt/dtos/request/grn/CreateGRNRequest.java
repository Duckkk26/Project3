package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNReceiveStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNStatus;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO yêu cầu để tạo một phiếu nhập kho mới.
 */
@Data
public class CreateGRNRequest {
    /**
     * ID của phiếu nhập kho.
     * Trường này có thể không cần thiết khi tạo mới và thường được hệ thống tự động sinh ra.
     */
    @JsonProperty("sub_id")
    @Schema(description = "ID của phiếu nhập kho", example = "GRN12345", required = false)
    private String subId;

    /**
     * Ngày dự kiến giao hàng.
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("expected_delivery_at")
    @Schema(description = "Ngày dự kiến giao hàng", example = "2023-09-15", required = false)
    private LocalDate expectedDeliveryAt;

    /**
     * ID của nhà cung cấp liên quan đến phiếu nhập kho.
     * Đây là thông tin bắt buộc.
     */
    @NotNull(message = MessageValidateKeys.SUPPLIER_NOT_NULL)
    @JsonProperty("supplier_id")
    @Schema(description = "ID của nhà cung cấp", example = "SUP12345", required = true)
    private String supplierId;

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
     * Trạng thái của phiếu nhập kho.
     * Đây là thông tin bắt buộc.
     */
    @JsonProperty("status")
    @Schema(description = "Trạng thái của phiếu nhập kho", example = "PENDING", required = true)
    private GRNStatus status;

    /**
     * Số tiền giảm giá cho phiếu nhập kho.
     * Đây là thông tin bắt buộc.
     */
   // @ValidNumber(message = MessageValidateKeys.GRN_DISCOUNT_NOT_NEGATIVE)
    @JsonProperty("discount")
    @Schema(description = "Số tiền giảm giá cho phiếu nhập kho", example = "500000.00", required = true)
    private BigDecimal discount;

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
     * Danh sách các sản phẩm liên quan đến phiếu nhập kho.
     * Đây là thông tin bắt buộc.
     */
    @NotEmpty(message = MessageValidateKeys.GRN_PRODUCTS_NOT_EMPTY)
    @JsonProperty("products")
    @Schema(description = "Danh sách các sản phẩm", required = true)
    private List<CreateGRNProductRequest> products;

    /**
     * ID của đơn hàng liên quan đến phiếu nhập kho.
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("order_id")
    @Schema(description = "ID của đơn hàng", example = "ORD12345", required = false)
    private String orderId;

    /**
     * Trạng thái nhận hàng của phiếu nhập kho.
     * Đây là thông tin tùy chọn.
     */
    @NotNull(message = MessageValidateKeys.GRN_RECEIVED_STATUS_NOT_NULL)
    @JsonProperty("received_status")
    private GRNReceiveStatus receivedStatus;
}
