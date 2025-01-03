package com.sapo.mock_project.inventory_receipt.repositories.supplier;

import com.sapo.mock_project.inventory_receipt.entities.SupplierGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SupplierGroupRepository extends JpaRepository<SupplierGroup, String>, JpaSpecificationExecutor<SupplierGroup> {
    boolean existsByNameAndTenantId(String name, String tenantId);

    boolean existsBySubIdAndTenantId(String subId, String tenantId);

    Optional<SupplierGroup> findByIdAndTenantId(String id, String tenantId);
}
