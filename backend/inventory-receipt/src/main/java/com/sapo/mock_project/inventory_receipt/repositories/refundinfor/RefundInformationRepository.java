package com.sapo.mock_project.inventory_receipt.repositories.refundinfor;

import com.sapo.mock_project.inventory_receipt.entities.GRN;
import com.sapo.mock_project.inventory_receipt.entities.RefundInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundInformationRepository extends JpaRepository<RefundInformation, String> {
    List<RefundInformation> findAllByGrnAndTenantId(GRN grn, String tenantId);
}
