package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.GetListProductManageRequest;
import com.sapo.mock_project.inventory_receipt.entities.Product;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductManageSpecification implements Specification<Product> {
    private GetListProductManageRequest request;
    private String tenantId;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        String keyword = request.getKeyword();
        List<String> categoryIds = request.getCategoryIds();
        LocalDateTime createdDateFrom = DateUtils.getDateTimeFrom(request.getCreatedDateFrom());
        LocalDateTime createdDateTo = DateUtils.getDateTimeFrom(request.getCreatedDateTo());
        List<String> brandIds = request.getBrandIds();
        List<ProductStatus> statuses = request.getStatuses();

        if (keyword != null) {
            String keywordUpper = keyword.toUpperCase();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                    String.format("%%%s%%", keywordUpper));
            Predicate subIdPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("subId")),
                    String.format("%%%s%%", keywordUpper));

            predicates.add(criteriaBuilder.or(namePredicate, subIdPredicate));
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categoryIds));
        }

        if (createdDateFrom != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdDateFrom));
        }

        if (createdDateTo != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdDateTo));
        }

        if (brandIds != null && !brandIds.isEmpty()) {
            predicates.add(root.get("brand").get("id").in(brandIds));
        }

        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(root.get("status").in(statuses));
        }
        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
