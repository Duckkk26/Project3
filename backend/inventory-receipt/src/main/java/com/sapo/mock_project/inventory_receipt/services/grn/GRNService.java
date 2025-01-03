package com.sapo.mock_project.inventory_receipt.services.grn;

import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.GetListGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.UpdateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GRNService {
    ResponseEntity<ResponseObject<Object>> createGRN(CreateGRNRequest request);

    ResponseEntity<ResponseObject<Object>> getGRNById(String id);

    ResponseEntity<ResponseObject<Object>> updateGRN(String id, UpdateGRNRequest request);

    ResponseEntity<ResponseObject<Object>> deleteGRN(String id);

    ResponseEntity<ResponseObject<Object>> filterGRN(GetListGRNRequest request,
                                                     Map<String, Boolean> filterParams,
                                                     int page, int size);

    ResponseEntity<ResponseObject<Object>> importGRN(String id);

    ResponseEntity<ResponseObject<Object>> getAllBySupplier(String supplierId, int page, int size);

    ResponseEntity<ResponseObject<Object>> getAllByOrder(String orderId, int page, int size);

    ResponseEntity<ResponseObject<Object>> exportData(GetListGRNRequest request, String mode);
}
