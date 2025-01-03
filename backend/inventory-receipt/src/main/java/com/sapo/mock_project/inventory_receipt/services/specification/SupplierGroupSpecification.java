package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierGroupStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.GetListSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.entities.SupplierGroup;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierGroupSpecification implements Specification<SupplierGroup> {
    private GetListSupplierGroupRequest request;
    private String tenantId;

    @Override
    public Predicate toPredicate(Root<SupplierGroup> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        String keyword = request.getKeyword();
        SupplierGroupStatus status = request.getStatus();

        if (keyword != null && !keyword.isEmpty()) {
            String keywordUpper = keyword.toUpperCase();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                    String.format("%%%s%%", keywordUpper));
            Predicate subIdPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("subId")),
                    String.format("%%%s%%", keywordUpper));

            predicates.add(criteriaBuilder.or(namePredicate, subIdPredicate));
        }

        if (status != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }

        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
