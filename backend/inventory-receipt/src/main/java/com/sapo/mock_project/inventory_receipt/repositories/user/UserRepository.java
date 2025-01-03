package com.sapo.mock_project.inventory_receipt.repositories.user;

import com.sapo.mock_project.inventory_receipt.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByPhoneAndTenantId(String username, String tenantId);

    Optional<User> findByPhone(String phone);

    Optional<User> findByIdAndTenantId(String id, String tenantId);

    boolean existsByPhoneAndTenantId(String phone, String tenantId);

    boolean existsByPhone(String phone);

    @Query("SELECT u.id, u.fullName FROM User u WHERE u.tenantId = :tenantId")
    Page<Object[]> findAllFullNameAndTenantId(String tenantId, Pageable pageable);

    @Query("""
                   SELECT
                      (SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'INCOME' AND t.tenantId = :tenantId) AS total_income,
                     (SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE' AND t.tenantId = :tenantId) AS total_expense,
                    (SELECT COUNT(*) FROM Order o WHERE (o.status = 'PENDING' OR o.status = 'PARTIAL') AND o.tenantId = :tenantId) AS count_order,
                    (SELECT COUNT(*) FROM GRN g WHERE (g.status = 'ORDERING' OR g.status = 'TRADING') AND g.tenantId = :tenantId) AS count_grn,
                    (SELECT COUNT(*) FROM GIN g WHERE g.status = 'CHECKING' AND g.tenantId = :tenantId) AS count_gin,
                    (SELECT COUNT(*) FROM Product p WHERE p.status = 'ACTIVE' AND p.tenantId = :tenantId) AS count_product,
                    (SELECT COALESCE(SUM(p.quantity), 0) FROM Product p WHERE p.status = 'ACTIVE' AND p.tenantId = :tenantId) AS sum_quantity
            
            """)
    List<Object[]> getDashboardData(String tenantId);
}
