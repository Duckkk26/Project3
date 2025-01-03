package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.CreateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.GetListSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.UpdateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.supplier.SupplierGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý nhóm nhà cung cấp.
 * Cung cấp các API liên quan đến việc tạo, cập nhật, lấy chi tiết và xóa nhóm nhà cung cấp.
 */
@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.SUPPLIER_GROUP)
@RequiredArgsConstructor
@Tag(name = "Supplier Group", description = "Các API quản lý nhóm nhà cung cấp")
public class SupplierGroupController {
    private final SupplierGroupService supplierGroupService;

    /**
     * API tạo mới nhóm nhà cung cấp.
     *
     * @param request Yêu cầu tạo nhóm nhà cung cấp.
     * @return Đối tượng phản hồi chứa thông tin nhóm nhà cung cấp được tạo.
     */
    @Operation(summary = "Tạo mới nhóm nhà cung cấp", description = "API này cho phép tạo mới một nhóm nhà cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nhóm nhà cung cấp đã được tạo thành công",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ", content = @Content)
    })
    @PostMapping("/create.json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<ResponseObject<Object>> createSupplierGroup(@Valid @RequestBody CreateSupplierGroupRequest request) {
        return supplierGroupService.createSupplierGroup(request);
    }

    /**
     * API lấy thông tin chi tiết của nhóm nhà cung cấp dựa trên ID.
     *
     * @param id ID của nhóm nhà cung cấp.
     * @return Đối tượng phản hồi chứa thông tin chi tiết của nhóm nhà cung cấp.
     */
    @Operation(summary = "Lấy thông tin chi tiết nhóm nhà cung cấp", description = "API này trả về chi tiết của một nhóm nhà cung cấp dựa trên ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thông tin chi tiết nhóm nhà cung cấp",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
            @ApiResponse(responseCode = "404", description = "Nhóm nhà cung cấp không tồn tại", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ", content = @Content)
    })
    @GetMapping("/detail.json/{id}")
    public ResponseEntity<ResponseObject<Object>> getSupplierGroupById(@PathVariable(value = "id") String id) {
        return supplierGroupService.getSupplierGroupById(id);
    }

    /**
     * API lấy danh sách tất cả các nhóm nhà cung cấp đang hoạt động.
     *
     * @param page Trang hiện tại, mặc định là 1.
     * @param size Kích thước của trang, mặc định là 10.
     * @return Đối tượng phản hồi chứa danh sách các nhóm nhà cung cấp.
     */
    @Operation(summary = "Lấy danh sách nhóm nhà cung cấp", description = "API này trả về danh sách các nhóm nhà cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách nhóm nhà cung cấp",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ", content = @Content)
    })
    @PostMapping("/all.json")
    public ResponseEntity<ResponseObject<Object>> getAllSupplierGroup(@Valid @RequestBody GetListSupplierGroupRequest request,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return supplierGroupService.getAllSupplierGroup(request, page, size);
    }

    /**
     * API cập nhật thông tin của nhóm nhà cung cấp dựa trên ID.
     *
     * @param id      ID của nhóm nhà cung cấp.
     * @param request Yêu cầu cập nhật nhóm nhà cung cấp.
     * @return Đối tượng phản hồi chứa thông tin nhóm nhà cung cấp đã được cập nhật.
     */
    @Operation(summary = "Cập nhật nhóm nhà cung cấp", description = "API này cho phép cập nhật thông tin của một nhóm nhà cung cấp dựa trên ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nhóm nhà cung cấp đã được cập nhật thành công",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nhóm nhà cung cấp không tồn tại", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ", content = @Content)
    })
    @PutMapping("/update.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<ResponseObject<Object>> updateSupplierGroup(@PathVariable(value = "id") String id,
                                                                      @Valid @RequestBody UpdateSupplierGroupRequest request) {
        return supplierGroupService.updateSupplierGroup(id, request);
    }

    /**
     * API xóa nhóm nhà cung cấp dựa trên ID.
     *
     * @param id ID của nhóm nhà cung cấp.
     * @return Đối tượng phản hồi xác nhận việc xóa nhóm nhà cung cấp.
     */
    @Operation(summary = "Xóa nhóm nhà cung cấp", description = "API này cho phép xóa một nhóm nhà cung cấp dựa trên ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nhóm nhà cung cấp đã được xóa thành công",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))),
            @ApiResponse(responseCode = "404", description = "Nhóm nhà cung cấp không tồn tại", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ", content = @Content)
    })
    @DeleteMapping("/delete.json/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<ResponseObject<Object>> deleteSupplierGroup(@PathVariable(value = "id") String id) {
        return supplierGroupService.deleteSupplierGroup(id);
    }
}
