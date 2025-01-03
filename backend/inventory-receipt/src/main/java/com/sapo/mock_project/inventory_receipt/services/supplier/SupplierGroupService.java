package com.sapo.mock_project.inventory_receipt.services.supplier;

import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.CreateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.GetListSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.UpdateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface SupplierGroupService {
    ResponseEntity<ResponseObject<Object>> createSupplierGroup(CreateSupplierGroupRequest request);

    ResponseEntity<ResponseObject<Object>> getSupplierGroupById(String supplierGroupId);

    ResponseEntity<ResponseObject<Object>> getAllSupplierGroup(GetListSupplierGroupRequest request, int page, int size);

    ResponseEntity<ResponseObject<Object>> updateSupplierGroup(String supplierGroupId, UpdateSupplierGroupRequest request);

    ResponseEntity<ResponseObject<Object>> deleteSupplierGroup(String supplierGroupId);
}
