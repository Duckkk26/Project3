package com.sapo.mock_project.inventory_receipt.repositories.grn;

import com.sapo.mock_project.inventory_receipt.dtos.response.grn.GRNGetListResponse;

import java.time.LocalDate;
import java.util.List;

public interface GRNRepositoryCustom {
    List<GRNGetListResponse> getFilterGRN(String filterJson, String keyword,
                                          String statuses, String receivedStatus,
                                          String supplierIds, String productIds,
                                          LocalDate startCreatedAt, LocalDate endCreatedAt,
                                          LocalDate startExpectedAt, LocalDate endExpectedAt,
                                          String userCreatedIds, String userCompletedIds,
                                          String userCancelledIds, String tenantId,
                                          int page, int size);

    int countTotalGRN(String filterJson, String keyword,
                      String statuses, String receivedStatus,
                      String supplierIds, String productIds,
                      LocalDate startCreatedAt, LocalDate endCreatedAt,
                      LocalDate startExpectedAt, LocalDate endExpectedAt,
                      String userCreatedIds, String userCompletedIds,
                      String userCancelledIds, String tenantId);
}
