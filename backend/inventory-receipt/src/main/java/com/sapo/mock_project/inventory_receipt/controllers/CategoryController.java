package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.category.CreateCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.category.GetListCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.category.CategoryService;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các yêu cầu liên quan đến danh mục (category).
 * Định tuyến các yêu cầu HTTP đến các phương thức tương ứng trong CategoryService.
 */
@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.CATEGORY)
@RequiredArgsConstructor
@Tag(name = "Category", description = "API quản lý danh mục")
public class CategoryController {

    // Service quản lý các thao tác liên quan đến danh mục
    private final CategoryService categoryService;

    /**
     * API tạo mới một danh mục.
     *
     * @param request Đối tượng chứa thông tin yêu cầu tạo mới danh mục.
     * @return ResponseEntity chứa đối tượng ResponseObject, phản hồi kết quả thực hiện.
     */
    @Operation(summary = "Tạo mới danh mục", description = "API để tạo mới một danh mục trong hệ thống.")
    @PostMapping("/create.json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<ResponseObject<Object>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        StringUtils.trimAllStringFields(request);

        return categoryService.createCategory(request);
    }

    /**
     * API lấy danh sách các danh mục có phân trang và lọc theo yêu cầu.
     *
     * @param request Đối tượng chứa các tiêu chí lọc danh mục.
     * @param page    Số trang yêu cầu (mặc định là 1).
     * @param size    Số lượng danh mục trên mỗi trang (mặc định là 10).
     * @return ResponseEntity chứa đối tượng ResponseObject với danh sách danh mục và thông tin phân trang.
     */
    @Operation(summary = "Lấy danh sách danh mục", description = "API để lấy danh sách các danh mục có phân trang và tiêu chí lọc.")
    @PostMapping("/all.json")
    public ResponseEntity<ResponseObject<Object>> getListCategory(@Valid @RequestBody GetListCategoryRequest request,
                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        return categoryService.getListCategory(request, page, size);
    }
}
