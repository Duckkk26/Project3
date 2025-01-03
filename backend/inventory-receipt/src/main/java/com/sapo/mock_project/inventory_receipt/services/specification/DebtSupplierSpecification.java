package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListDebtSupplierRequest;
import com.sapo.mock_project.inventory_receipt.entities.DebtSupplier;
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
public class DebtSupplierSpecification implements Specification<DebtSupplier> {
    private GetListDebtSupplierRequest request;
    private String tenantId;

    @Override
    public Predicate toPredicate(Root<DebtSupplier> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        String supplierId = request.getSupplierId();
        LocalDateTime createdDateFrom = DateUtils.getDateTimeFrom(request.getCreatedDateFrom());
        LocalDateTime createdDateTo = DateUtils.getDateTimeFrom(request.getCreatedDateTo());

        if (supplierId != null) {
            predicates.add(criteriaBuilder.equal(root.get("supplier").get("id"), supplierId));
        }
        if (createdDateFrom != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), createdDateFrom));
        }
        if (createdDateTo != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), createdDateTo));
        }
        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
