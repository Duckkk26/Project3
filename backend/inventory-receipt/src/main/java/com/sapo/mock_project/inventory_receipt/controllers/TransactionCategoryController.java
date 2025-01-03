package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.CreateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.GetListTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.UpdateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.transaction.TransactionCategoryService;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý danh mục phiếu thu/chi.
 * <p>
 * Lớp này cung cấp các API để tạo, lấy danh sách và cập nhật danh mục phiếu thu/chi.
 * </p>
 */
@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.TRANSACTION_CATEGORY)
@RequiredArgsConstructor
@Tag(name = "Transaction Category", description = "Các API quản lý danh mục phiếu thu/chi")
public class TransactionCategoryController {
    private final TransactionCategoryService transactionCategoryService;

    /**
     * Tạo một danh mục phiếu thu/chi mới.
     * <p>
     * API này tạo một danh mục phiếu thu/chi mới dựa trên thông tin được cung cấp trong yêu cầu.
     * </p>
     *
     * @param request thông tin cần thiết để tạo danh mục phiếu thu/chi
     * @return ResponseEntity chứa kết quả của việc tạo danh mục phiếu thu/chi
     */
    @PostMapping("/create.json")
    @Operation(summary = "Tạo danh mục phiếu thu/chi", description = "Tạo một danh mục phiếu thu/chi mới.")
    @ApiResponse(responseCode = "201", description = "Danh mục phiếu thu/chi được tạo thành công")
    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    public ResponseEntity<ResponseObject<Object>> createTransactionCategory(@Valid @RequestBody CreateTransactionCategoryRequest request) {
        StringUtils.trimAllStringFields(request);

        return transactionCategoryService.createTransactionCategory(request);
    }

    /**
     * Lấy danh sách danh mục phiếu thu/chi.
     * <p>
     * API này lấy danh sách các danh mục phiếu thu/chi dựa trên các tiêu chí tìm kiếm và phân trang.
     * </p>
     *
     * @param request thông tin tìm kiếm danh mục phiếu thu/chi
     * @param page số trang để phân trang
     * @param size kích thước trang
     * @return ResponseEntity chứa danh sách danh mục phiếu thu/chi và thông tin phân trang
     */
    @PostMapping("/all.json")
    @Operation(summary = "Lấy danh sách danh mục phiếu thu/chi", description = "Lấy danh sách các danh mục phiếu thu/chi với phân trang.")
    @ApiResponse(responseCode = "200", description = "Danh sách danh mục phiếu thu/chi được lấy thành công")
    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    public ResponseEntity<ResponseObject<Object>> getListTransactionCategory(@RequestBody GetListTransactionCategoryRequest request,
                                                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        return transactionCategoryService.getListTransactionCategory(request, page, size);
    }

    /**
     * Cập nhật danh mục phiếu thu/chi hiện có.
     * <p>
     * API này cập nhật thông tin của một danh mục phiếu thu/chi hiện có dựa trên ID và thông tin mới.
     * </p>
     *
     * @param id ID của danh mục phiếu thu/chi cần cập nhật
     * @param request thông tin cập nhật cho danh mục phiếu thu/chi
     * @return ResponseEntity chứa kết quả của việc cập nhật danh mục phiếu thu/chi
     */
    @PutMapping("/update.json/{id}")
    @Operation(summary = "Cập nhật danh mục phiếu thu/chi", description = "Cập nhật thông tin của một danh mục phiếu thu/chi hiện có.")
    @ApiResponse(responseCode = "200", description = "Danh mục phiếu thu/chi được cập nhật thành công")
    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    @ApiResponse(responseCode = "404", description = "Danh mục phiếu thu/chi không tìm thấy")
    public ResponseEntity<ResponseObject<Object>> updateTransactionCategory(@PathVariable String id,
                                                                            @Valid @RequestBody UpdateTransactionCategoryRequest request) {
        StringUtils.trimAllStringFields(request);

        return transactionCategoryService.updateTransactionCategory(id, request);
    }
}
