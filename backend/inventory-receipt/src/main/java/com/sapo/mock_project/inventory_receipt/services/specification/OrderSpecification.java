package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.GetListOrderRequest;
import com.sapo.mock_project.inventory_receipt.entities.Order;
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
public class OrderSpecification implements Specification<Order> {
    private GetListOrderRequest request;
    private String tenantId;

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        String keyword = request.getKeyword();
        List<OrderStatus> statuses = request.getStatuses();
        List<String> supplierIds = request.getSupplierIds();
        LocalDateTime startCreatedAt = DateUtils.getDateTimeFrom(request.getStartCreatedAt());
        LocalDateTime endCreatedAt = DateUtils.getDateTimeTo(request.getEndCreatedAt());
        LocalDateTime startExpectedAt = DateUtils.getDateTimeFrom(request.getStartExpectedAt());
        LocalDateTime endExpectedAt = DateUtils.getDateTimeTo(request.getEndExpectedAt());
        List<String> productIds = request.getProductIds();
        List<String> userCreatedIds = request.getUserCreatedIds();
        List<String> userCompletedIds = request.getUserCompletedIds();
        List<String> userCancelledIds = request.getUserCancelledIds();

        if (keyword != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("id"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("subId"), "%" + keyword + "%")
            ));
        }

        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(root.get("status").in(statuses));
        }

        if (supplierIds != null && !supplierIds.isEmpty()) {
            predicates.add(root.get("supplier").get("id").in(supplierIds));
        }

        if (startCreatedAt != null) {
            Predicate startCreatedAtPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startCreatedAt);
            predicates.add(startCreatedAtPredicate);
        }

        if (endCreatedAt != null) {
            Predicate endCreatedAtPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endCreatedAt);
            predicates.add(endCreatedAtPredicate);
        }

        if (startExpectedAt != null) {
            Predicate startExpectedAtPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("expectedAt"), startExpectedAt);
            predicates.add(startExpectedAtPredicate);
        }

        if (endExpectedAt != null) {
            Predicate endExpectedAtPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("expectedAt"), endExpectedAt);
            predicates.add(endExpectedAtPredicate);
        }

        if (productIds != null && !productIds.isEmpty()) {
            predicates.add(root.join("orderDetails").get("product").get("id").in(productIds));
        }

        if (userCreatedIds != null && !userCreatedIds.isEmpty()) {
            predicates.add(root.get("userCreated").get("id").in(userCreatedIds));
        }

        if (userCompletedIds != null && !userCompletedIds.isEmpty()) {
            predicates.add(root.get("userCompleted").get("id").in(userCompletedIds));
        }

        if (userCancelledIds != null && !userCancelledIds.isEmpty()) {
            predicates.add(root.get("userCancelled").get("id").in(userCancelledIds));
        }

        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
