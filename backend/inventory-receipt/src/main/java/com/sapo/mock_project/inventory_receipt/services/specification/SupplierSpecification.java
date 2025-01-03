package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListSupplierRequest;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
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
 * Lớp định nghĩa các điều kiện lọc cho đối tượng {@link Supplier} sử dụng JPA Criteria API.
 * Các điều kiện lọc được dựa trên thông tin từ đối tượng {@link GetListSupplierRequest}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierSpecification implements Specification<Supplier> {

    /**
     * Đối tượng chứa các thông tin lọc.
     */
    private GetListSupplierRequest request;

    private String tenantId;

    /**
     * Tạo ra các predicate (điều kiện) lọc dựa trên các tham số từ {@link GetListSupplierRequest}.
     *
     * @param root            Cơ sở của truy vấn, đại diện cho đối tượng {@link Supplier}.
     * @param query           Truy vấn mà các điều kiện sẽ được áp dụng.
     * @param criteriaBuilder Công cụ để xây dựng các điều kiện lọc.
     * @return Điều kiện lọc được kết hợp thành một điều kiện duy nhất.
     */
    @Override
    public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Lấy các thông tin lọc từ đối tượng request
        String keyword = request.getKeyword();
        List<SupplierStatus> statuses = request.getStatuses();
        List<String> supplierGroupIds = request.getSupplierGroupIds();
        LocalDateTime createdDateFrom = DateUtils.getDateTimeFrom(request.getCreatedDateFrom());
        LocalDateTime createdDateTo = DateUtils.getDateTimeTo(request.getCreatedDateTo());
        String tags = request.getTags();

        // Điều kiện lọc theo từ khóa
        if (keyword != null && !keyword.isEmpty()) {
            String keywordUpper = keyword.toUpperCase();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                    String.format("%%%s%%", keywordUpper));

            Predicate phonePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("phone")),
                    String.format("%%%s%%", keywordUpper));

            Predicate idPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("subId")),
                    String.format("%%%s%%", keywordUpper));

            predicates.add(criteriaBuilder.or(namePredicate, phonePredicate, idPredicate));
        }

        // Loại trừ các nhà cung cấp đã bị xóa
        predicates.add(criteriaBuilder.notEqual(root.get("status"), SupplierStatus.DELETED));

        // Điều kiện lọc theo trạng thái
        if (statuses != null && !statuses.isEmpty()) {
            Predicate customStatus = root.get("status").in(statuses);
            predicates.add(customStatus);
        }

        // Điều kiện lọc theo nhóm nhà cung cấp
        if (supplierGroupIds != null && !supplierGroupIds.isEmpty()) {
            Predicate customSupplierGroupId = root.get("supplierGroup").get("id").in(supplierGroupIds);
            predicates.add(customSupplierGroupId);
        }

        // Điều kiện lọc theo ngày tạo
        if (createdDateFrom != null) {
            Predicate customCreatedDateFrom = criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdDateFrom);
            predicates.add(customCreatedDateFrom);
        }
        if (createdDateTo != null) {
            Predicate customCreatedDateTo = criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdDateTo);
            predicates.add(customCreatedDateTo);
        }

        // Điều kiện lọc theo tags
        if (tags != null && !tags.isEmpty()) {
            Predicate customTags = criteriaBuilder.like(criteriaBuilder.upper(root.get("tags")),
                    String.format("%%%s%%", tags.toUpperCase()));
            predicates.add(customTags);
        }

        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        // Kết hợp tất cả các điều kiện bằng phép AND
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
