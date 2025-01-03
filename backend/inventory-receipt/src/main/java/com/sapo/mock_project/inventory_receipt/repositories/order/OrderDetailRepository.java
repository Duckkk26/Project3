package com.sapo.mock_project.inventory_receipt.repositories.order;

import com.sapo.mock_project.inventory_receipt.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
