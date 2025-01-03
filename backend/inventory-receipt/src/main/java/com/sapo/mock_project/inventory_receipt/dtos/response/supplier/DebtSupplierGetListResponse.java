package com.sapo.mock_project.inventory_receipt.dtos.response.supplier;

import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebtSupplierGetListResponse extends BaseResponse {
    private String referenceCode;

    private String referenceId;

    private String userCreatedName;

    private String note;

    private BigDecimal amount;

    private BigDecimal debtAfter;
}
