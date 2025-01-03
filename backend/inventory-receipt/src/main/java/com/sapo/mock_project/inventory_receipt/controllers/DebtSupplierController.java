package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListDebtSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.supplier.DebtSupplierService;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.DEBT_SUPPLIER)
@RequiredArgsConstructor
public class DebtSupplierController {
    private final DebtSupplierService debtSupplierService;

    @PostMapping("/filter.json")
    public ResponseEntity<ResponseObject<Object>> filterDebtSupplier(@Valid @RequestBody GetListDebtSupplierRequest request,
                                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return debtSupplierService.filterDebtSupplier(request, page, size);
    }
}
