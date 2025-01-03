package com.sapo.mock_project.inventory_receipt.repositories.brand;

import com.sapo.mock_project.inventory_receipt.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, String>, JpaSpecificationExecutor<Brand> {
    boolean existsByNameAndTenantId(String name, String tenantId);

    Optional<Brand> findByIdAndTenantId(String id, String tenantId);
}
