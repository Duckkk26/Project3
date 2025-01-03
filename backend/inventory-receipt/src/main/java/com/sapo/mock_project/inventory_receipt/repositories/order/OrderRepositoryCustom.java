package com.sapo.mock_project.inventory_receipt.repositories.order;

import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderGetListResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {
    List<OrderGetListResponse> getFilteredOrders(String filterJson, String keyword,
                                                 String statuses, String supplierIds,
                                                 LocalDate startCreatedAt, LocalDate endCreatedAt,
                                                 LocalDate startExpectedAt, LocalDate endExpectedAt,
                                                 String productIds, String userCreatedIds,
                                                 String userCompletedIds, String userCancelledIds,
                                                 String tenantId,
                                                 int page, int size);


    int countTotalOrders(String filterJson, String keyword,
                         String statuses, String supplierIds,
                         LocalDate startCreatedAt, LocalDate endCreatedAt,
                         LocalDate startExpectedAt, LocalDate endExpectedAt,
                         String productIds, String userCreatedIds,
                         String userCompletedIds, String userCancelledIds, String tenantId);
}
