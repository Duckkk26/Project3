package com.sapo.mock_project.inventory_receipt.services.brand;

import com.sapo.mock_project.inventory_receipt.dtos.request.brand.CreateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.GetListBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.UpdateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface BrandService {
    ResponseEntity<ResponseObject<Object>> createBrand(CreateBrandRequest request);

    ResponseEntity<ResponseObject<Object>> getBrandById(String brandId);

    ResponseEntity<ResponseObject<Object>> getAllBrands(GetListBrandRequest request, int page, int size);

    ResponseEntity<ResponseObject<Object>> updateBrand(String brandId, UpdateBrandRequest request);
}
