package com.sapo.mock_project.inventory_receipt.repositories.transaction;

import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, String>, JpaSpecificationExecutor<TransactionCategory> {
    boolean existsByNameAndTypeAndTenantId(String name, TransactionType type, String tenantId);

    boolean existsBySubIdAndTenantId(String subId, String tenantId);

    Optional<TransactionCategory> findByIdAndTenantId(String id, String tenantId);

    Optional<TransactionCategory> findBySubIdAndTenantId(String subId, String tenantId);
}
