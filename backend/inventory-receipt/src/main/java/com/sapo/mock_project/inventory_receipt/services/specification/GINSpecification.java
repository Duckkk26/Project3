package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.GetListGINRequest;
import com.sapo.mock_project.inventory_receipt.entities.GIN;
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
public class GINSpecification implements Specification<GIN> {
    private GetListGINRequest request;
    private String tenantId;

    @Override
    public Predicate toPredicate(Root<GIN> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        String keyword = request.getKeyword();
        List<GINStatus> statuses = request.getStatuses();
        LocalDateTime createdDateFrom = DateUtils.getDateTimeFrom(request.getCreatedDateFrom());
        LocalDateTime createdDateTo = DateUtils.getDateTimeTo(request.getCreatedDateTo());
        LocalDateTime balancedDateFrom = DateUtils.getDateTimeFrom(request.getBalancedDateFrom());
        LocalDateTime balancedDateTo = DateUtils.getDateTimeTo(request.getBalancedDateTo());
        List<String> userCreatedIds = request.getUserCreatedIds();
        List<String> userBalancedIds = request.getUserBalancedIds();
        List<String> userInspectionIds = request.getUserInspectionIds();

        if (keyword != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("sub_id"), "%" + keyword + "%")
            ));
        }
        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(root.get("status").in(statuses));
        }
        if (createdDateFrom != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdDateFrom));
        }
        if (createdDateTo != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdDateTo));
        }
        if (balancedDateFrom != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("balancedAt"), balancedDateFrom));
        }
        if (balancedDateTo != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("balancedAt"), balancedDateTo));
        }
        if (userCreatedIds != null && !userCreatedIds.isEmpty()) {
            predicates.add(root.get("userCreated").get("id").in(userCreatedIds));
        }
        if (userBalancedIds != null && !userBalancedIds.isEmpty()) {
            predicates.add(root.get("userBalanced").get("id").in(userBalancedIds));
        }
        if (userInspectionIds != null && !userInspectionIds.isEmpty()) {
            predicates.add(root.get("userInspection").get("id").in(userInspectionIds));
        }
        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
