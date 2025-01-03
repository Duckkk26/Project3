package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.constants.NameFilterFromCookie;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.product.ProductService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.PRODUCT)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/quick-create.json")
    public ResponseEntity<ResponseObject<Object>> quickCreateProduct(@Valid @RequestBody QuickCreateProductRequest request) {
        StringUtils.trimAllStringFields(request);

        return productService.quickCreateProduct(request);
    }

    @PostMapping("/create.json")
    public ResponseEntity<ResponseObject<Object>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        StringUtils.trimAllStringFields(request);

        return productService.createProduct(request);
    }

    @PutMapping("/update.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> updateProduct(@PathVariable String id,
                                                                @Valid @RequestBody UpdateProductRequest request) {
        StringUtils.trimAllStringFields(request);

        return productService.updateProduct(id, request);
    }

    @PostMapping("/filter.json")
    public ResponseEntity<ResponseObject<Object>> filterProduct(@RequestBody GetListProductRequest request,
                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                                HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.PRODUCT, httpServletRequest);

        return productService.filterProduct(request, filterParams, page, size);
    }

    @PostMapping("/filter-warehouse.json")
    public ResponseEntity<ResponseObject<Object>> filterProductInWarehouse(@RequestBody GetListProductManageRequest request,
                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                                HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.PRODUCT_MANAGE, httpServletRequest);

        return productService.filterProductInWarehouse(request, filterParams, page, size);
    }

    @GetMapping("/quick-get-list.json")
    public ResponseEntity<ResponseObject<Object>> quickGetListProduct(@RequestParam String keyword,
                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return productService.quickGetListProduct(keyword, page, size);
    }

    @DeleteMapping("/delete.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/detail.json/{id}")
    public ResponseEntity<ResponseObject<Object>> getDetailProduct(@PathVariable String id) {
        return productService.getDetailProduct(id);
    }
}
