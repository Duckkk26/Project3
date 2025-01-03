package com.sapo.mock_project.inventory_receipt.dtos.response.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Lớp DTO đại diện cho phản hồi khi lấy danh sách phiếu thu/chi.
 * Cung cấp các thông tin chi tiết của phiếu thu/chi như ID, loại phiếu thu/chi, trạng thái,
 * số tiền, nhóm người nhận, mã tham chiếu, phương thức thanh toán, ghi chú và người tạo phiếu thu/chi.
 */
@Data
public class TransactionGetListResponse extends BaseResponse {

    /**
     * Mã định danh của phiếu thu/chi.
     */
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Mã định danh của phiếu thu/chi", example = "TX123456")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    /**
     * Loại phiếu thu/chi (nhập kho, xuất kho, vv.).
     */
    @JsonProperty("transaction_category_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Loại phiếu thu/chi", example = "DEPOSIT")
    private String transactionCategoryName;

    /**
     * Trạng thái phiếu thu/chi (đang xử lý, đã hoàn thành, đã hủy).
     */
    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Trạng thái phiếu thu/chi", example = "COMPLETED")
    private TransactionStatus status;

    /**
     * Số tiền trong phiếu thu/chi.
     */
    @JsonProperty("amount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Số tiền trong phiếu thu/chi", example = "1000.00")
    private BigDecimal amount;

    /**
     * Nhóm người nhận của phiếu thu/chi.
     */
    @JsonProperty("recipient_group")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Nhóm người nhận của phiếu thu/chi", example = "Group A")
    private String recipientGroup;

    /**
     * Mã định danh của người nhận trong phiếu thu/chi.
     */
    @JsonProperty("recipient_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Mã định danh của người nhận", example = "R123456")
    private String recipientId;

    /**
     * Tên của người nhận trong phiếu thu/chi.
     */
    @JsonProperty("recipient_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Tên của người nhận", example = "Nguyễn Văn A")
    private String recipientName;

    /**
     * Mã tham chiếu liên quan đến phiếu thu/chi.
     */
    @JsonProperty("reference_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Mã tham chiếu liên quan đến phiếu thu/chi", example = "REF123456")
    private String referenceCode;

    /**
     * Mã định danh tham chiếu liên quan đến phiếu thu/chi.
     */
    @JsonProperty("reference_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Mã định danh tham chiếu", example = "REFID123456")
    private String referenceId;

    /**
     * Phương thức thanh toán của phiếu thu/chi.
     */
    @JsonProperty("payment_method")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Phương thức thanh toán của phiếu thu/chi", example = "CREDIT_CARD")
    private TransactionMethod paymentMethod;

    /**
     * Ghi chú đi kèm phiếu thu/chi (nếu có).
     */
    @JsonProperty("note")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Ghi chú đi kèm phiếu thu/chi", example = "Ghi chú thêm về phiếu thu/chi")
    private String note;

    /**
     * Tên người đã tạo phiếu thu/chi.
     */
    @JsonProperty("user_created_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Tên người đã tạo phiếu thu/chi", example = "Nguyễn Văn B")
    private String userCreatedName;
}
