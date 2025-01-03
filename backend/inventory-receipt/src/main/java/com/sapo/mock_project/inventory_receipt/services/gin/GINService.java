package com.sapo.mock_project.inventory_receipt.services.gin;

import com.sapo.mock_project.inventory_receipt.dtos.request.gin.CreateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.GetListGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.UpdateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GINService {
    ResponseEntity<ResponseObject<Object>> createGIN(CreateGINRequest request);

    ResponseEntity<ResponseObject<Object>> getGINById(String id);

    ResponseEntity<ResponseObject<Object>> updateGIN(String id, UpdateGINRequest request);

    ResponseEntity<ResponseObject<Object>> deleteGIN(String id);

    ResponseEntity<ResponseObject<Object>> filterGIN(GetListGINRequest request, Map<String, Boolean> filterParams, int page, int size);

    ResponseEntity<ResponseObject<Object>> balanceGIN(String id);

    ResponseEntity<ResponseObject<Object>> exportData(GetListGINRequest request, String mode);
}
