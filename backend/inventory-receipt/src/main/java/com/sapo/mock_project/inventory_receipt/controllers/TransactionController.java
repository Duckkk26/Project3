package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.constants.NameFilterFromCookie;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.transaction.TransactionService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller để quản lý các phiếu thu/chi.
 * Cung cấp các API để tạo mới, cập nhật, hủy và lọc phiếu thu/chi.
 */
@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.TRANSACTION)
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Các API quản lý phiếu thu/chi")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Tạo mới một phiếu thu/chi.
     *
     * @param request đối tượng chứa thông tin của phiếu thu/chi mới
     * @return phản hồi với kết quả của việc tạo phiếu thu/chi
     */
    @PostMapping("/create.json")
    @Operation(summary = "Tạo phiếu thu/chi mới", description = "Tạo mới một phiếu thu/chi với thông tin cung cấp trong yêu cầu")
    @ApiResponse(responseCode = "200", description = "Phiếu thu/chi được tạo thành công")
    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    public ResponseEntity<ResponseObject<Object>> createReceipt(@Valid @RequestBody CreateTransactionRequest request) {
        StringUtils.trimAllStringFields(request);

        return transactionService.createTransaction(request);
    }

    /**
     * Lọc danh sách phiếu thu/chi dựa trên các tham số lọc và phân trang.
     *
     * @param request            đối tượng chứa thông tin lọc phiếu thu/chi
     * @param sort               kiểu sắp xếp (ASC hoặc DESC)
     * @param sortField          trường để sắp xếp
     * @param page               số trang hiện tại
     * @param size               kích thước trang
     * @param httpServletRequest đối tượng yêu cầu HTTP để lấy thông tin cookie
     * @return phản hồi với danh sách phiếu thu/chi đã lọc
     */
    @PostMapping("/filter.json")
    @Operation(summary = "Lọc danh sách phiếu thu/chi", description = "Lọc danh sách phiếu thu/chi dựa trên các tham số và phân trang")
    @ApiResponse(responseCode = "200", description = "Danh sách phiếu thu/chi đã lọc")
    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    public ResponseEntity<ResponseObject<Object>> filterTransaction(@Valid @RequestBody GetListTransactionRequest request,
                                                                    @RequestParam(defaultValue = "ASC") @Parameter(description = "Kiểu sắp xếp, mặc định là ASC") String sort,
                                                                    @RequestParam(value = "sort_field", defaultValue = "createdAt") @Parameter(description = "Trường để sắp xếp, mặc định là createdAt") String sortField,
                                                                    @RequestParam(defaultValue = "1") @Parameter(description = "Số trang hiện tại, mặc định là 1") int page,
                                                                    @RequestParam(defaultValue = "10") @Parameter(description = "Kích thước trang, mặc định là 10") int size,
                                                                    HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.TRANSACTION, httpServletRequest);

        return transactionService.filterTransaction(request, filterParams, sort, sortField, page, size);
    }

    /**
     * Cập nhật thông tin của một phiếu thu/chi.
     *
     * @param id      ID của phiếu thu/chi cần cập nhật
     * @param request đối tượng chứa thông tin cập nhật phiếu thu/chi
     * @return phản hồi với kết quả của việc cập nhật phiếu thu/chi
     */
    @PatchMapping("/update.json/{id}")
    @Operation(summary = "Cập nhật phiếu thu/chi", description = "Cập nhật thông tin của một phiếu thu/chi dựa trên ID")
    @ApiResponse(responseCode = "200", description = "Phiếu thu/chi được cập nhật thành công")
    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    @ApiResponse(responseCode = "404", description = "Phiếu thu/chi không tìm thấy")
    public ResponseEntity<ResponseObject<Object>> updateTransaction(@PathVariable String id,
                                                                    @Valid @RequestBody UpdateTransactionRequest request) {
        StringUtils.trimAllStringFields(request);

        return transactionService.updateTransaction(id, request);
    }

    /**
     * Hủy một phiếu thu/chi.
     *
     * @param id ID của phiếu thu/chi cần hủy
     * @return phản hồi với kết quả của việc hủy phiếu thu/chi
     */
    @PatchMapping("/cancel.json/{id}")
    @Operation(summary = "Hủy phiếu thu/chi", description = "Hủy một phiếu thu/chi dựa trên ID")
    @ApiResponse(responseCode = "200", description = "Phiếu thu/chi được hủy thành công")
    @ApiResponse(responseCode = "404", description = "Phiếu thu/chi không tìm thấy")
    public ResponseEntity<ResponseObject<Object>> cancelTransaction(@PathVariable String id) {
        return transactionService.cancelTransaction(id);
    }

    @PostMapping("/total.json")
    public ResponseEntity<ResponseObject<Object>> getTotalTransaction(@RequestBody GetTotalRequest request,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return transactionService.getTotalTransaction(request, page, size);
    }

    @PostMapping("/payment-grn.json")
    public ResponseEntity<ResponseObject<Object>> paymentGRN(@RequestBody CreateTransactionGRNRequest request) {
        return transactionService.paymentGRN(request);
    }
}
