package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.constants.NameFilterFromCookie;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.CreateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.GetListGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.UpdateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.gin.GINService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.GIN)
@RequiredArgsConstructor
@Slf4j
public class GINController {
    private final GINService ginService;

    @PostMapping("/create.json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> createGIN(@Valid @RequestBody CreateGINRequest request) {
        StringUtils.trimAllStringFields(request);

        return ginService.createGIN(request);
    }

    @GetMapping("/detail.json/{id}")
    public ResponseEntity<ResponseObject<Object>> getGINById(@PathVariable("id") String id) {
        return ginService.getGINById(id);
    }

    @PutMapping("/update.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> updateGIN(@PathVariable String id,
                                                            @Valid @RequestBody UpdateGINRequest request) {
        StringUtils.trimAllStringFields(request);

        return ginService.updateGIN(id, request);
    }

    @DeleteMapping("/delete.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> deleteGIN(@PathVariable("id") String id) {
        return ginService.deleteGIN(id);
    }

    @PostMapping("/filter.json")
    public ResponseEntity<ResponseObject<Object>> filterGIN(@RequestBody GetListGINRequest request,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.GIN, httpServletRequest);

        return ginService.filterGIN(request, filterParams, page, size);
    }

    @PutMapping("balance.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> balanceGIN(@PathVariable String id) {
        return ginService.balanceGIN(id);
    }

    @PostMapping("/export-data.json")
    public ResponseEntity<ResponseObject<Object>> exportData(@RequestBody GetListGINRequest request,
                                                             @RequestParam(defaultValue = "DEFAULT") String mode) {
        return ginService.exportData(request, mode);
    }
}
