package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.GetListTransactionRequest;
import com.sapo.mock_project.inventory_receipt.entities.Transaction;
import com.sapo.mock_project.inventory_receipt.utils.DateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này triển khai Specification để xây dựng truy vấn động cho thực thể Transaction
 * dựa trên các tiêu chí lọc được cung cấp qua đối tượng GetListTransactionRequest.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSpecification implements Specification<Transaction> {
    private GetListTransactionRequest request; // Yêu cầu chứa các tiêu chí lọc cho phiếu thu/chi.

    private String tenantId; // ID của khách hàng sử dụng dịch vụ.

    /**
     * Phương thức này xây dựng các điều kiện (Predicate) dựa trên các tiêu chí lọc của yêu cầu.
     *
     * @param root Tham chiếu đến đối tượng Transaction trong cơ sở dữ liệu.
     * @param query Truy vấn cần xây dựng.
     * @param criteriaBuilder Hỗ trợ tạo các điều kiện trong câu truy vấn.
     * @return Predicate chứa các điều kiện lọc cho truy vấn.
     */
    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // Tạo danh sách chứa các điều kiện lọc
        List<Predicate> predicates = new ArrayList<>();

        // Lấy các tham số lọc từ request
        String keyword = request.getKeyword();
        TransactionType type = request.getType();
        List<String> recipientGroups = request.getRecipientGroups();
        LocalDateTime createdDateFrom = DateUtils.getDateTimeFrom(request.getCreatedDateFrom());
        LocalDateTime createdDateTo = DateUtils.getDateTimeTo(request.getCreatedDateTo());
        LocalDateTime updatedDateFrom = DateUtils.getDateTimeFrom(request.getUpdatedDateFrom());
        LocalDateTime updatedDateTo = DateUtils.getDateTimeTo(request.getUpdatedDateTo());
        LocalDateTime cancelledDateFrom = DateUtils.getDateTimeFrom(request.getCancelledDateFrom());
        LocalDateTime cancelledDateTo = DateUtils.getDateTimeTo(request.getCancelledDateTo());
        List<TransactionMethod> paymentMethods = request.getPaymentMethods();
        List<String> categoryIds = request.getCategoryIds();
        List<String> createdUserIds = request.getCreatedUserIds();
        List<TransactionStatus> statuses = request.getStatuses();

        // Lọc theo từ khóa
        if (keyword != null && !keyword.isEmpty()) {
            String keywordUpper = keyword.toUpperCase();
            Predicate codePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("id")),
                    String.format("%%%s%%", keywordUpper));
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("referenceId")),
                    String.format("%%%s%%", keywordUpper));
            predicates.add(criteriaBuilder.or(codePredicate, descriptionPredicate));
        }

        if (type != null) {
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
        }

        // Lọc theo nhóm người nhận
        if (recipientGroups != null && !recipientGroups.isEmpty()) {
            predicates.add(root.get("recipientGroup").in(recipientGroups));
        }

        // Lọc theo thời gian tạo
        if (createdDateFrom != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdDateFrom));
        }
        if (createdDateTo != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdDateTo));
        }

        // Lọc theo thời gian cập nhật
        if (updatedDateFrom != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), updatedDateFrom));
        }
        if (updatedDateTo != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), updatedDateTo));
        }

        // Lọc theo thời gian hủy
        if (cancelledDateFrom != null || cancelledDateTo != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), TransactionStatus.CANCELLED));
            if (cancelledDateFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), cancelledDateFrom));
            }
            if (cancelledDateTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), cancelledDateTo));
            }
        }

        // Lọc theo phương thức thanh toán
        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            predicates.add(root.get("paymentMethod").in(paymentMethods));
        }

        // Lọc theo danh mục phiếu thu/chi
        if (categoryIds != null && !categoryIds.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categoryIds));
        }

        // Lọc theo người tạo phiếu thu/chi
        if (createdUserIds != null && !createdUserIds.isEmpty()) {
            predicates.add(root.get("userCreated").get("id").in(createdUserIds));
        }

        // Lọc theo trạng thái phiếu thu/chi
        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(root.get("status").in(statuses));
        }

        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        // Kết hợp các điều kiện lọc
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
