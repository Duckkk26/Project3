package com.sapo.mock_project.inventory_receipt.repositories.refundinfor;

import com.sapo.mock_project.inventory_receipt.entities.RefundInformationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundDetailRepository extends JpaRepository<RefundInformationDetail, String> {
}
