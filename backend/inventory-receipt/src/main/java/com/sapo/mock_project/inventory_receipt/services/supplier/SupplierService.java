package com.sapo.mock_project.inventory_receipt.services.supplier;

import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.CreateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.UpdateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface SupplierService {
    ResponseEntity<ResponseObject<Object>> createSupplier(CreateSupplierRequest request);

    ResponseEntity<ResponseObject<Object>> getSupplierById(String supplierId);

    ResponseEntity<ResponseObject<Object>> filterSupplier(GetListSupplierRequest request, Map<String, Boolean> filterParams, int page, int size);

    ResponseEntity<ResponseObject<Object>> updateSupplier(String supplierId, UpdateSupplierRequest request);

    ResponseEntity<ResponseObject<Object>> deleteSupplier(String supplierId);

    ResponseEntity<ResponseObject<Object>> getListNameSupplier(String name, int page, int size);

    ResponseEntity<ResponseObject<Object>> getDetailMoney(String supplierId);

    ResponseEntity<ResponseObject<Object>> exportData(GetListSupplierRequest code, String mode);
}
