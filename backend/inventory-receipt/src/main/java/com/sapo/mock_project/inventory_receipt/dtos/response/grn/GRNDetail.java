package com.sapo.mock_project.inventory_receipt.dtos.response.grn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.constants.enums.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNHistory;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO phản hồi chi tiết thông tin của một phiếu nhập kho (GRN).
 * Kế thừa từ {@link BaseResponse} để cung cấp các thông tin cơ bản về phản hồi.
 */
@Data
public class GRNDetail extends BaseResponse {

    /**
     * ID của phiếu nhập kho.
     */
    @Schema(description = "ID của phiếu nhập kho", example = "GRN12345")
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    /**
     * Trạng thái của phiếu nhập kho.
     */
    @Schema(description = "Trạng thái của phiếu nhập kho", example = "PENDING")
    @JsonProperty("status")
    private GRNStatus status;

    @JsonProperty("received_status")
    private GRNReceiveStatus receivedStatus;

    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    /**
     * Ngày dự kiến giao hàng.
     */
    @Schema(description = "Ngày dự kiến giao hàng", example = "2023-09-15")
    @JsonProperty("expected_delivery_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime expectedDeliveryAt;

    /**
     * Ngày nhận hàng.
     */
    @Schema(description = "Ngày nhận hàng", example = "2023-09-20")
    @JsonProperty("received_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime receivedAt;

    /**
     * Thời gian thanh toán.
     */
    @Schema(description = "Thời gian thanh toán", example = "2023-09-20T15:30:00")
    @JsonProperty("payment_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime paymentAt;

    /**
     * Tổng số lượng nhận hàng.
     */
    @Schema(description = "Tổng số lượng nhận hàng", example = "100")
    @JsonProperty("total_received_quantity")
    private BigDecimal totalReceivedQuantity;

    /**
     * Chiết khấu cho phiếu nhập kho.
     */
    @Schema(description = "Chiết khấu cho phiếu nhập kho", example = "5000.00")
    @JsonProperty("discount")
    private BigDecimal discount;

    /**
     * Số tiền thuế cho phiếu nhập kho.
     */
    @Schema(description = "Số tiền thuế cho phiếu nhập kho", example = "500000.00")
    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    /**
     * Tổng giá trị của phiếu nhập kho.
     */
    @Schema(description = "Tổng giá trị của phiếu nhập kho", example = "1500000.00")
    @JsonProperty("total_value")
    private BigDecimal totalValue;

    @JsonProperty("total_paid")
    private BigDecimal totalPaid;

    /**
     * Trạng thái thanh toán của phiếu nhập kho.
     */
    @Schema(description = "Trạng thái thanh toán của phiếu nhập kho", example = "PAID")
    @JsonProperty("payment_status")
    private GRNPaymentStatus paymentStatus;

    /**
     * Trạng thái trả hàng của phiếu nhập kho.
     */
    @Schema(description = "Trạng thái trả hàng của phiếu nhập kho", example = "NOT_RETURNED")
    @JsonProperty("return_status")
    private ReturnStatus returnStatus;

    /**
     * Trạng thái hoàn trả của phiếu nhập kho.
     */
    @Schema(description = "Trạng thái hoàn trả của phiếu nhập kho", example = "NOT_REFUNDED")
    @JsonProperty("refund_status")
    private GRNRefundStatus refundStatus;

    /**
     * Ghi chú của phiếu nhập kho.
     */
    @Schema(description = "Ghi chú của phiếu nhập kho", example = "Phiếu nhập hàng lần đầu từ nhà cung cấp mới")
    @JsonProperty("note")
    private String note;

    /**
     * Các thẻ liên quan đến phiếu nhập kho.
     */
    @Schema(description = "Các thẻ liên quan đến phiếu nhập kho", example = "Nhập hàng, Ưu tiên")
    @JsonProperty("tags")
    private String tags;

    /**
     * Chi phí nhập khẩu của phiếu nhập kho.
     */
    @Schema(description = "Chi phí nhập khẩu của phiếu nhập kho")
    @JsonProperty("import_cost")
    private List<GRNImportCost> importCosts;

    /**
     * Phương thức thanh toán của phiếu nhập kho.
     */
    @Schema(description = "Phương thức thanh toán của phiếu nhập kho")
    @JsonProperty("payment_method")
    private List<GRNPaymentMethod> paymentMethods;

    @JsonProperty("histories")
    private List<GRNHistory> histories;

    /**
     * Danh sách sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "Danh sách sản phẩm trong phiếu nhập kho")
    @JsonProperty("products")
    private List<GRNProductDetail> products;
}
