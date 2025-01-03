package com.sapo.mock_project.inventory_receipt.repositories.gin;

import com.sapo.mock_project.inventory_receipt.entities.GINProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GINProductRepository extends JpaRepository<GINProduct, String> {
}
