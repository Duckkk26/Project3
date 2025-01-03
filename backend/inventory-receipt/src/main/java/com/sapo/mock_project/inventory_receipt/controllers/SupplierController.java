package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.constants.NameFilterFromCookie;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.CreateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.UpdateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.supplier.SupplierService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Bộ điều khiển cho các hoạt động liên quan đến nhà cung cấp.
 * Cung cấp các API để tạo, lấy thông tin, lọc, cập nhật và xóa nhà cung cấp.
 */
@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.SUPPLIER)
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    /**
     * Tạo một nhà cung cấp mới.
     *
     * @param request Yêu cầu chứa thông tin của nhà cung cấp cần tạo.
     * @return ResponseEntity chứa thông tin phản hồi về kết quả tạo nhà cung cấp.
     */
    @PostMapping("/create.json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<ResponseObject<Object>> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        StringUtils.trimAllStringFields(request);

        return supplierService.createSupplier(request);
    }

    /**
     * Lấy thông tin chi tiết của một nhà cung cấp theo ID.
     *
     * @param id ID của nhà cung cấp cần lấy thông tin.
     * @return ResponseEntity chứa thông tin chi tiết về nhà cung cấp.
     */
    @GetMapping("/detail.json/{id}")
    public ResponseEntity<ResponseObject<Object>> getSupplierById(@PathVariable("id") String id) {
        return supplierService.getSupplierById(id);
    }

    /**
     * Lọc danh sách nhà cung cấp dựa trên các tiêu chí và phân trang.
     *
     * @param request            Yêu cầu chứa các tiêu chí lọc nhà cung cấp.
     * @param page               Trang hiện tại để phân trang.
     * @param size               Kích thước trang để phân trang.
     * @param httpServletRequest Đối tượng HttpServletRequest để lấy các tham số lọc từ cookie.
     * @return ResponseEntity chứa danh sách nhà cung cấp sau khi lọc và phân trang.
     */
    @PostMapping("/filter.json")
    public ResponseEntity<ResponseObject<Object>> filterSupplier(@RequestBody GetListSupplierRequest request,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.SUPPLIER, httpServletRequest);

        return supplierService.filterSupplier(request, filterParams, page, size);
    }

    /**
     * Cập nhật thông tin của một nhà cung cấp theo ID.
     *
     * @param id      ID của nhà cung cấp cần cập nhật.
     * @param request Yêu cầu chứa thông tin cập nhật của nhà cung cấp.
     * @return ResponseEntity chứa thông tin phản hồi về kết quả cập nhật nhà cung cấp.
     */
    @PutMapping("/update.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<ResponseObject<Object>> updateSupplier(@PathVariable String id,
                                                                 @Valid @RequestBody UpdateSupplierRequest request) {
        StringUtils.trimAllStringFields(request);

        return supplierService.updateSupplier(id, request);
    }

    /**
     * Xóa một nhà cung cấp theo ID.
     *
     * @param id ID của nhà cung cấp cần xóa.
     * @return ResponseEntity chứa thông tin phản hồi về kết quả xóa nhà cung cấp.
     */
    @DeleteMapping("/delete.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<ResponseObject<Object>> deleteSupplier(@PathVariable("id") String id) {
        return supplierService.deleteSupplier(id);
    }

    /**
     * Lấy danh sách tên nhà cung cấp.
     *
     * @param page Trang hiện tại để phân trang.
     * @param size Kích thước trang để phân trang.
     * @return ResponseEntity chứa danh sách tên nhà cung cấp.
     */
    @GetMapping("/list-name.json")
    public ResponseEntity<ResponseObject<Object>> getListNameSupplier(@RequestParam(defaultValue = "") String keyword,
                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        return supplierService.getListNameSupplier(keyword, page, size);
    }

    @GetMapping("/detail-money.json/{id}")
    public ResponseEntity<ResponseObject<Object>> getDetailMoney(@PathVariable String id) {
        return supplierService.getDetailMoney(id);
    }

    @PostMapping("/export-data.json")
    public ResponseEntity<ResponseObject<Object>> exportData(@RequestBody GetListSupplierRequest request,
                                                             @RequestParam(defaultValue = "DEFAULT") String mode) {
        return supplierService.exportData(request, mode);
    }
}
