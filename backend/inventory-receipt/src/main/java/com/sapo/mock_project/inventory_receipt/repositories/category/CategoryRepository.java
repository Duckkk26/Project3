package com.sapo.mock_project.inventory_receipt.repositories.category;

import com.sapo.mock_project.inventory_receipt.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {
    boolean existsByNameAndTenantId(String name, String tenantId);

    Optional<Category> findByIdAndTenantId(String id, String tenantId);
}
