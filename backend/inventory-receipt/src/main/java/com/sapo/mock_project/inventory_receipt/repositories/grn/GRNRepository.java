package com.sapo.mock_project.inventory_receipt.repositories.grn;

import com.sapo.mock_project.inventory_receipt.entities.GRN;
import com.sapo.mock_project.inventory_receipt.entities.Order;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GRNRepository extends JpaRepository<GRN, String>, JpaSpecificationExecutor<GRN> {
    boolean existsBySubIdAndTenantId(String subId, String tenantId);

    Optional<GRN> findByIdAndTenantId(String id, String tenantId);

    Page<GRN> findBySupplierAndTenantId(Supplier supplier, String tenantId, Pageable pageable);

    Page<GRN> findByOrderAndTenantId(Order order, String tenantId, Pageable pageable);
}
