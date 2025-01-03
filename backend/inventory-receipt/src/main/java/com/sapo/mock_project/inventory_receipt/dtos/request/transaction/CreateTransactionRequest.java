package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Lớp DTO đại diện cho yêu cầu tạo mới phiếu thu/chi.
 * Chứa thông tin cần thiết để tạo một phiếu thu/chi mới.
 */
@Data
public class CreateTransactionRequest {

    /**
     * Mã phiếu thu/chi (ID) của phiếu thu/chi.
     * Trong trường hợp này, mã phiếu thu/chi có thể được hệ thống sinh tự động.
     */
    @JsonProperty("sub_id")
    @Schema(description = "Mã phiếu thu/chi (ID) của phiếu thu/chi", example = "TX123456")
    private String subId;

    /**
     * Số tiền của phiếu thu/chi.
     */
    @JsonProperty("amount")
    @Schema(description = "Số tiền của phiếu thu/chi", example = "1000.00")
    private BigDecimal amount;

    @JsonProperty("payment_method")
    private TransactionMethod paymentMethod;

    /**
     * Các tag liên quan đến phiếu thu/chi.
     */
    @JsonProperty("tags")
    @Schema(description = "Các tag liên quan đến phiếu thu/chi", example = "['urgent', 'office']")
    private String tags;

    /**
     * Ghi chú hoặc mô tả về phiếu thu/chi.
     */
    @JsonProperty("note")
    @Schema(description = "Ghi chú hoặc mô tả về phiếu thu/chi", example = "Thanh toán cho hóa đơn tháng 8")
    private String note;

    @JsonProperty("type")
    private TransactionType type;

    @JsonProperty("recipient_group")
    private String recipientGroup;

    @JsonProperty("recipient_id")
    private String recipientId;

    @JsonProperty("recipient_name")
    private String recipientName;

    @JsonProperty("transaction_category_id")
    private String transactionCategoryId;

    @JsonProperty("is_auto_debt")
    private boolean isAutoDebt;
}
