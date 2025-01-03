package com.sapo.mock_project.inventory_receipt.services.category;

import com.sapo.mock_project.inventory_receipt.dtos.request.category.CreateCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.request.category.GetListCategoryRequest;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<ResponseObject<Object>> createCategory(CreateCategoryRequest request);

    ResponseEntity<ResponseObject<Object>> getListCategory(GetListCategoryRequest request, int page, int size);
}
