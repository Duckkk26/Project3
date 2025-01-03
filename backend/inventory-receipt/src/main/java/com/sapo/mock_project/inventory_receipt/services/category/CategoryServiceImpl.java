package com.sapo.mock_project.inventory_receipt.services.category;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.dtos.request.category.CreateCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.category.GetListCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.category.CategoryGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.Category;
import com.sapo.mock_project.inventory_receipt.mappers.CategoryMapper;
import com.sapo.mock_project.inventory_receipt.repositories.category.CategoryRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.CategorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service xử lý các thao tác liên quan đến danh mục (category).
 * Sử dụng các phương thức từ repository để truy xuất và thao tác với dữ liệu danh mục.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    // Repository để làm việc với dữ liệu danh mục trong cơ sở dữ liệu
    private final CategoryRepository categoryRepository;

    // Mapper để chuyển đổi dữ liệu giữa entity và DTO
    private final CategoryMapper categoryMapper;

    // Utility để xử lý việc lấy message được bản địa hóa (localization)
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    /**
     * Phương thức tạo mới danh mục.
     *
     * @param request Đối tượng chứa dữ liệu yêu cầu tạo mới danh mục.
     * @return ResponseEntity chứa đối tượng ResponseObject, trả về thông báo và kết quả thực hiện.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createCategory(CreateCategoryRequest request) {
        try {
            // Kiểm tra nếu tên danh mục đã tồn tại
            if (categoryRepository.existsByNameAndTenantId(request.getName(), authHelper.getUser().getTenantId())) {
                // Trả về thông báo lỗi nếu tên danh mục đã tồn tại
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.CATEGORY_NAME_EXISTED));
            }

            // Ánh xạ từ request DTO sang entity
            Category newCategory = categoryMapper.mapToEntity(request);

            // Lưu danh mục mới vào cơ sở dữ liệu
            categoryRepository.save(newCategory);

            // Trả về thông báo thành công khi tạo danh mục mới
            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            // Trả về thông báo lỗi nếu có ngoại lệ xảy ra
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Phương thức lấy danh sách danh mục theo yêu cầu và phân trang.
     *
     * @param request Đối tượng chứa các tiêu chí lọc danh mục.
     * @param page    Số trang yêu cầu.
     * @param size    Số lượng danh mục trên mỗi trang.
     * @return ResponseEntity chứa đối tượng ResponseObject với danh sách danh mục và thông tin phân trang.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getListCategory(GetListCategoryRequest request, int page, int size) {
        try {
            // Sử dụng Specification để tạo tiêu chí tìm kiếm danh mục
            CategorySpecification categorySpecification = new CategorySpecification(request, authHelper.getUser().getTenantId());

            // Tạo đối tượng Pageable để phân trang và sắp xếp theo tên danh mục tăng dần
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "name"));

            // Truy vấn danh sách danh mục từ cơ sở dữ liệu dựa trên tiêu chí tìm kiếm và phân trang
            Page<Category> categoryPage = categoryRepository.findAll(categorySpecification, pageable);

            // Chuyển đổi danh sách entity thành danh sách DTO response
            List<CategoryGetListResponse> categoryGetListResponses = categoryPage.getContent().stream()
                    .map(categoryMapper::mapToResponse)
                    .toList();

            Pagination pagination = Pagination.<Object>builder()
                    .data(categoryGetListResponses)
                    .totalPage(categoryPage.getTotalPages())
                    .totalItems(categoryPage.getTotalElements())
                    .build();

            // Trả về thông báo thành công kèm danh sách danh mục
            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            // Trả về thông báo lỗi nếu có ngoại lệ xảy ra
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
