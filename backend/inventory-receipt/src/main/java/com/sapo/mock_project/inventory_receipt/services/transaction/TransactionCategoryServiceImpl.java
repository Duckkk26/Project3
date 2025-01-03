package com.sapo.mock_project.inventory_receipt.services.transaction;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.CreateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.GetListTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.UpdateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.transaction.TransactionCategoryGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import com.sapo.mock_project.inventory_receipt.mappers.TransactionCategoryMapper;
import com.sapo.mock_project.inventory_receipt.repositories.transaction.TransactionCategoryRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.TransactionCategorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Dịch vụ để quản lý danh mục phiếu thu/chi (Transaction Category), bao gồm các thao tác tạo, cập nhật,
 * và lấy danh sách các danh mục phiếu thu/chi.
 */
@Service
@RequiredArgsConstructor
public class TransactionCategoryServiceImpl implements TransactionCategoryService {
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final TransactionCategoryMapper transactionCategoryMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    /**
     * Tạo một danh mục phiếu thu/chi mới.
     *
     * @param request Đối tượng yêu cầu chứa thông tin để tạo danh mục phiếu thu/chi
     * @return ResponseEntity chứa phản hồi của server sau khi thực hiện hành động
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createTransactionCategory(CreateTransactionCategoryRequest request) {
        try {
            // Kiểm tra xem ID có tồn tại không
            if (request.getSubId() != null && transactionCategoryRepository.existsBySubIdAndTenantId(request.getSubId(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.TRANSACTION_CATEGORY_ID_EXISTED));
            }

            // Kiểm tra xem tên danh mục và loại phiếu thu/chi có tồn tại không
            if (transactionCategoryRepository.existsByNameAndTypeAndTenantId(request.getName(), request.getType(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.TRANSACTION_CATEGORY_NAME_EXISTED));
            }

            // Chuyển đổi DTO thành entity TransactionCategory và lưu vào cơ sở dữ liệu
            TransactionCategory newTransactionCategory = transactionCategoryMapper.mapToEntity(request);
            transactionCategoryRepository.save(newTransactionCategory);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CATEGORY_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy danh sách các danh mục phiếu thu/chi dựa trên các tiêu chí lọc và phân trang.
     *
     * @param request Đối tượng chứa các thông tin yêu cầu để lọc danh mục phiếu thu/chi
     * @param page    Số trang muốn lấy
     * @param size    Số lượng bản ghi mỗi trang
     * @return ResponseEntity chứa phản hồi của server bao gồm danh sách danh mục phiếu thu/chi và thông tin phân trang
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getListTransactionCategory(GetListTransactionCategoryRequest request, int page, int size) {
        try {
            // Tạo Specification để lọc danh mục phiếu thu/chi dựa trên yêu cầu
            TransactionCategorySpecification specification = new TransactionCategorySpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "name"));

            // Lấy danh sách phân trang các danh mục phiếu thu/chi từ cơ sở dữ liệu
            Page<TransactionCategory> transactionCategoryPage = transactionCategoryRepository.findAll(specification, pageable);

            // Chuyển đổi entity TransactionCategory sang DTO TransactionCategoryGetListResponse
            Page<TransactionCategoryGetListResponse> responsePage = transactionCategoryPage.map(transactionCategoryMapper::mapToResponse);

            // Tạo đối tượng Pagination để chứa dữ liệu và thông tin phân trang
            Pagination response = Pagination.<Object>builder()
                    .data(responsePage.getContent())
                    .totalPage(responsePage.getTotalPages())
                    .totalItems(responsePage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CATEGORY_GET_ALL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin của danh mục phiếu thu/chi.
     *
     * @param transactionCategoryId ID của danh mục phiếu thu/chi cần cập nhật
     * @param request               Đối tượng chứa thông tin cập nhật
     * @return ResponseEntity chứa phản hồi của server sau khi thực hiện hành động
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateTransactionCategory(String transactionCategoryId, UpdateTransactionCategoryRequest request) {
        try {
            // Tìm kiếm danh mục phiếu thu/chi theo ID
            TransactionCategory existTransactionCategory = transactionCategoryRepository.findByIdAndTenantId(transactionCategoryId, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new Exception(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_CATEGORY_NOT_FOUND)));

            // Kiểm tra xem tên mới có trùng với bất kỳ danh mục phiếu thu/chi nào khác cùng loại không
            if (!existTransactionCategory.getName().equals(request.getName())
                && transactionCategoryRepository.existsByNameAndTypeAndTenantId(request.getName(), existTransactionCategory.getType(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.TRANSACTION_CATEGORY_NAME_EXISTED));
            }

            // Cập nhật thông tin danh mục phiếu thu/chi và lưu lại vào cơ sở dữ liệu
            transactionCategoryMapper.updateFromToDTO(request, existTransactionCategory);
            transactionCategoryRepository.save(existTransactionCategory);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CATEGORY_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
