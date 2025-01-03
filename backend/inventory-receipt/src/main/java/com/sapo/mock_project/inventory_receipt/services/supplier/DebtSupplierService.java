package com.sapo.mock_project.inventory_receipt.services.supplier;

import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListDebtSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface DebtSupplierService {
    ResponseEntity<ResponseObject<Object>> filterDebtSupplier(GetListDebtSupplierRequest request, int page, int size);
}
