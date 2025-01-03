package com.sapo.mock_project.inventory_receipt.services.product;

import com.sapo.mock_project.inventory_receipt.dtos.request.product.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductService {
    ResponseEntity<ResponseObject<Object>> quickCreateProduct(QuickCreateProductRequest request);

    ResponseEntity<ResponseObject<Object>> createProduct(CreateProductRequest request);

    ResponseEntity<ResponseObject<Object>> updateProduct(String productId, UpdateProductRequest request);

    ResponseEntity<ResponseObject<Object>> filterProduct(GetListProductRequest request, Map<String, Boolean> filterParams, int page, int size);

    ResponseEntity<ResponseObject<Object>> filterProductInWarehouse(GetListProductManageRequest request, Map<String, Boolean> filterParams, int page, int size);

    ResponseEntity<ResponseObject<Object>> quickGetListProduct(String keyword, int page, int size);

    ResponseEntity<ResponseObject<Object>> deleteProduct(String productId);

    ResponseEntity<ResponseObject<Object>> getDetailProduct(String productId);
}
