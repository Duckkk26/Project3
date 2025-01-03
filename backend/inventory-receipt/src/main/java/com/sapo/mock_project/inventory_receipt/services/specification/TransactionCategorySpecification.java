package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.GetListTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification để tạo điều kiện truy vấn động cho các bản ghi của bảng TransactionCategory.
 * <p>
 * Lớp này xây dựng điều kiện lọc dựa trên các thông tin đầu vào được cung cấp qua đối tượng
 * {@link GetListTransactionCategoryRequest}, bao gồm từ khóa (keyword) và loại phiếu thu/chi (transaction type).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCategorySpecification implements Specification<TransactionCategory> {
    /**
     * Đối tượng request chứa các tiêu chí lọc cho truy vấn
     */
    private GetListTransactionCategoryRequest request;

    private String tenantId;

    /**
     * Phương thức xây dựng Predicate (tập các điều kiện truy vấn) dựa trên request.
     *
     * @param root            Gốc của truy vấn, đại diện cho entity TransactionCategory
     * @param query           Truy vấn cần thực thi
     * @param criteriaBuilder Công cụ hỗ trợ xây dựng truy vấn (Criteria API)
     * @return Predicate bao gồm các điều kiện lọc được kết hợp với phép AND
     */
    @Override
    public Predicate toPredicate(Root<TransactionCategory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // Tạo danh sách chứa các điều kiện lọc
        List<Predicate> predicates = new ArrayList<>();

        // Lấy keyword và type từ request
        String keyword = request.getKeyword();
        TransactionType type = request.getType();

        // Điều kiện lọc theo từ khóa (keyword)
        if (keyword != null && !keyword.isEmpty()) {
            String keywordUpper = keyword.toUpperCase(); // Chuyển từ khóa sang chữ hoa để so sánh không phân biệt hoa thường

            // Tạo điều kiện lọc theo tên (name) của TransactionCategory
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                    String.format("%%%s%%", keywordUpper)); // Sử dụng biểu thức LIKE với từ khóa

            // Tạo điều kiện lọc theo mã ID của TransactionCategory
            Predicate idPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("id")),
                    String.format("%%%s%%", keywordUpper)); // Sử dụng biểu thức LIKE với từ khóa

            // Kết hợp các điều kiện lọc theo tên và mã ID bằng phép OR
            predicates.add(criteriaBuilder.or(namePredicate, idPredicate));
        }

        // Điều kiện lọc theo loại phiếu thu/chi (type)
        if (type != null) {
            // Tạo điều kiện lọc bằng phép so sánh chính xác theo loại phiếu thu/chi
            Predicate customStatus = criteriaBuilder.equal(root.get("type"), type);
            predicates.add(customStatus);
        }

        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        // Kết hợp tất cả các điều kiện bằng phép AND
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
