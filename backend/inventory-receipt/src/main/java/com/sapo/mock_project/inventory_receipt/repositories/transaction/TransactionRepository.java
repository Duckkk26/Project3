package com.sapo.mock_project.inventory_receipt.repositories.transaction;

import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    boolean existsBySubIdAndTenantId(String subId, String tenantId);

    Optional<Transaction> findByIdAndTenantId(String id, String tenantId);

    @Query("SELECT t FROM Transaction t WHERE t.referenceCode = 'RI' AND t.referenceId = :grnId AND t.tenantId = :tenantId")
    List<Transaction> findByRefundIdAndTenantId(String grnId, String tenantId);

    @Query("""
                SELECT
                    SUM(CASE WHEN t.type = 'INCOME' AND (:dateType = 'CREATED_AT' AND t.createdAt BETWEEN :dateFrom AND :dateTo OR :dateType = 'UPDATED_AT' AND t.updatedAt BETWEEN :dateFrom AND :dateTo) THEN t.amount ELSE 0 END) AS totalIncome,
                    SUM(CASE WHEN t.type = 'EXPENSE' AND (:dateType = 'CREATED_AT' AND t.createdAt BETWEEN :dateFrom AND :dateTo OR :dateType = 'UPDATED_AT' AND t.updatedAt BETWEEN :dateFrom AND :dateTo) THEN t.amount ELSE 0 END) AS totalExpense,
                    SUM(CASE WHEN (:dateType = 'CREATED_AT' AND t.createdAt < :dateFrom OR :dateType = 'UPDATED_AT' AND t.updatedAt < :dateFrom) THEN t.amount ELSE 0 END) AS totalBefore
                FROM Transaction t
                WHERE t.tenantId = :tenantId
                AND (:mode IS NULL OR t.type = :mode)
            """)
    List<Object[]> getTotalValue(TransactionType mode, String dateType, LocalDateTime dateFrom, LocalDateTime dateTo, String tenantId);
}
