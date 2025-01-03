package com.sapo.mock_project.inventory_receipt.repositories.gin;

import com.sapo.mock_project.inventory_receipt.entities.GIN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface GINRepository extends JpaRepository<GIN, String>, JpaSpecificationExecutor<GIN> {
    boolean existsBySubIdAndTenantId(String subId, String tenantId);

    Optional<GIN> findByIdAndTenantId(String id, String tenantId);

    List<GIN> findALlByTenantId(String tenantId);
}
