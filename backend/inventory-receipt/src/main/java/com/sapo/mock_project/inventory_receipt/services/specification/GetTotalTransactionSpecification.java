package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.GetTotalRequest;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTotalTransactionSpecification implements Specification<Transaction> {
    private GetTotalRequest request;
    private String tenantId;

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        TransactionType mode = request.getMode();
        String keyword = request.getKeyword();
        List<String> userCreatedIds = request.getUserCreatedIds();
        List<TransactionMethod> paymentMethods = request.getPaymentMethods();
        String dateType = request.getDateType();
        LocalDateTime dateFrom = DateUtils.getDateTimeFrom(request.getDateFrom());
        LocalDateTime dateTo = DateUtils.getDateTimeTo(request.getDateTo());

        if (mode != null) {
            if (mode ==TransactionType.INCOME) {
                Predicate incomePredicate = criteriaBuilder.equal(root.get("type"), "INCOME");

                predicates.add(incomePredicate);
            } else if (mode == TransactionType.EXPENSE) {
                Predicate expensePredicate = criteriaBuilder.equal(root.get("type"), "EXPENSE");

                predicates.add(expensePredicate);
            }
        }

        if (keyword != null) {
            Predicate keywordPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("subId"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("referenceCode"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("recipientName"), "%" + keyword + "%")
            );

            predicates.add(keywordPredicate);
        }

        if (userCreatedIds != null && !userCreatedIds.isEmpty()) {
            Predicate userCreatedIdsPredicate = root.get("userCreated").get("id").in(userCreatedIds);

            predicates.add(userCreatedIdsPredicate);
        }

        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            Predicate paymentMethodsPredicate = root.get("paymentMethod").in(paymentMethods);

            predicates.add(paymentMethodsPredicate);
        }

        if (dateType.equals("CREATED_AT")) {
            Predicate createdAtPredicate = criteriaBuilder.between(root.get("createdAt"), dateFrom, dateTo);

            predicates.add(createdAtPredicate);
        } else if (dateType.equals("UPDATED_AT")) {
            Predicate updatedAtPredicate = criteriaBuilder.between(root.get("updatedAt"), dateFrom, dateTo);

            predicates.add(updatedAtPredicate);
        }

        if (tenantId != null) {
            Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), tenantId);

            predicates.add(tenantIdPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
