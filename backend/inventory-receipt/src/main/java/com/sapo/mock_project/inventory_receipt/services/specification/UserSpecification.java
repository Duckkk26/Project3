package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.GetListAccountRequest;
import com.sapo.mock_project.inventory_receipt.entities.User;
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
public class UserSpecification implements Specification<User> {
    private GetListAccountRequest request;

    private String tenantId;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        String keyword = request.getKeyword();
        List<RoleEnum> roles = request.getRoles();
        LocalDateTime startCreatedDate = DateUtils.getDateTimeFrom(request.getStartCreatedDate());
        LocalDateTime endCreatedDate = DateUtils.getDateTimeFrom(request.getEndCreatedDate());
        List<Boolean> activeStatuses = request.getActiveStatuses();

        if (keyword != null) {
            String keywordUpper = keyword.toUpperCase();
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.upper(root.get("fullName")), "%" + keywordUpper + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(root.get("email")), "%" + keywordUpper + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(root.get("phone")), "%" + keywordUpper + "%")
            ));
        }

        if (roles != null) {
            predicates.add(root.get("role").in(roles));
        }

        if (startCreatedDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startCreatedDate));
        }

        if (endCreatedDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endCreatedDate));
        }

        if (activeStatuses != null) {
            predicates.add(root.get("isActive").in(activeStatuses));
        }

        predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
