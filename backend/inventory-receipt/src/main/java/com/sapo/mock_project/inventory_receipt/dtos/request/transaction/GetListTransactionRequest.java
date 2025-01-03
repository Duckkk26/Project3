package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Lớp DTO đại diện cho yêu cầu tìm kiếm và lọc danh sách phiếu thu/chi.
 * Chứa các tiêu chí lọc và tìm kiếm để lấy danh sách phiếu thu/chi phù hợp.
 */
@Data
public class GetListTransactionRequest {

    /**
     * Từ khóa để tìm kiếm trong các phiếu thu/chi.
     */
    @JsonProperty("keyword")
    @Schema(description = "Từ khóa để tìm kiếm trong các phiếu thu/chi", example = "TX123456")
    private String keyword;

    /**
     * Loại phiếu thu/chi để lọc.
     */
    @JsonProperty("type")
    private TransactionType type;

    /**
     * Danh sách các nhóm người nhận để lọc phiếu thu/chi.
     */
    @JsonProperty("recipient_groups")
    @Schema(description = "Danh sách các nhóm người nhận để lọc phiếu thu/chi", example = "[\"Nhóm A\", \"Nhóm B\"]")
    private List<String> recipientGroups;

    /**
     * Danh sách các phương thức thanh toán để lọc phiếu thu/chi.
     */
    @JsonProperty("payment_methods")
    @Schema(description = "Danh sách các phương thức thanh toán để lọc phiếu thu/chi", example = "[\"CASH\", \"CARD\"]")
    private List<TransactionMethod> paymentMethods;

    /**
     * Ngày bắt đầu để lọc phiếu thu/chi theo ngày tạo.
     */
    @JsonProperty("created_date_from")
    @Schema(description = "Ngày bắt đầu để lọc phiếu thu/chi theo ngày tạo", example = "2023-01-01")
    private LocalDate createdDateFrom;

    /**
     * Ngày kết thúc để lọc phiếu thu/chi theo ngày tạo.
     */
    @JsonProperty("created_date_to")
    @Schema(description = "Ngày kết thúc để lọc phiếu thu/chi theo ngày tạo", example = "2023-12-31")
    private LocalDate createdDateTo;

    /**
     * Ngày bắt đầu để lọc phiếu thu/chi theo ngày cập nhật.
     */
    @JsonProperty("updated_date_from")
    @Schema(description = "Ngày bắt đầu để lọc phiếu thu/chi theo ngày cập nhật", example = "2023-01-01")
    private LocalDate updatedDateFrom;

    /**
     * Ngày kết thúc để lọc phiếu thu/chi theo ngày cập nhật.
     */
    @JsonProperty("updated_date_to")
    @Schema(description = "Ngày kết thúc để lọc phiếu thu/chi theo ngày cập nhật", example = "2023-12-31")
    private LocalDate updatedDateTo;

    /**
     * Ngày bắt đầu để lọc phiếu thu/chi theo ngày hủy.
     */
    @JsonProperty("cancelled_date_from")
    @Schema(description = "Ngày bắt đầu để lọc phiếu thu/chi theo ngày hủy", example = "2023-01-01")
    private LocalDate cancelledDateFrom;

    /**
     * Ngày kết thúc để lọc phiếu thu/chi theo ngày hủy.
     */
    @JsonProperty("cancelled_date_to")
    @Schema(description = "Ngày kết thúc để lọc phiếu thu/chi theo ngày hủy", example = "2023-12-31")
    private LocalDate cancelledDateTo;

    /**
     * Danh sách các mã danh mục để lọc phiếu thu/chi.
     */
    @JsonProperty("category_ids")
    @Schema(description = "Danh sách các mã danh mục để lọc phiếu thu/chi", example = "[\"CAT001\", \"CAT002\"]")
    private List<String> categoryIds;

    /**
     * Danh sách các ID người tạo để lọc phiếu thu/chi.
     */
    @JsonProperty("created_user_ids")
    @Schema(description = "Danh sách các ID người tạo để lọc phiếu thu/chi", example = "[\"USR001\", \"USR002\"]")
    private List<String> createdUserIds;

    /**
     * Danh sách các trạng thái phiếu thu/chi để lọc.
     */
    @JsonProperty("statuses")
    @Schema(description = "Danh sách các trạng thái phiếu thu/chi để lọc", example = "[\"PENDING\", \"COMPLETED\"]")
    private List<TransactionStatus> statuses;
}
