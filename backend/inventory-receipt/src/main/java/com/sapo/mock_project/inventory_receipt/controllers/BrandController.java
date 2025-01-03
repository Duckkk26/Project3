package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.CreateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.GetListBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.UpdateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.brand.BrandService;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.BRAND)
@RequiredArgsConstructor
@Tag(name = "Brand", description = "API quản lý thương hiệu")
public class BrandController {
    private final BrandService brandService;

    @PostMapping("/create.json")
    @Operation(summary = "Tạo mới thương hiệu")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> createBrand(@Valid @RequestBody CreateBrandRequest createBrandRequest) {
        StringUtils.trimAllStringFields(createBrandRequest);

        return brandService.createBrand(createBrandRequest);
    }

    @PutMapping("/update.json/{id}")
    @Operation(summary = "Cập nhật thông tin thương hiệu")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> updateBrand(@PathVariable String id, @RequestBody UpdateBrandRequest updateBrandRequest) {
        StringUtils.trimAllStringFields(updateBrandRequest);

        return brandService.updateBrand(id, updateBrandRequest);
    }

    @PostMapping("/all.json")
    @Operation(summary = "Lấy danh sách thương hiệu")
    public ResponseEntity<ResponseObject<Object>> getAllBrands(@RequestBody GetListBrandRequest request,
                                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return brandService.getAllBrands(request, page, size);
    }

    @GetMapping("/detail.json/{id}")
    @Operation(summary = "Lấy chi tiết thương hiệu")
    public ResponseEntity<ResponseObject<Object>> getBrandById(@PathVariable String id) {
        return brandService.getBrandById(id);
    }
}
