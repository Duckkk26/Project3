package com.sapo.mock_project.inventory_receipt.services.gin;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.ExportDataResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.GINDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.GINGetListResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.GINProductDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.*;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.GINMapper;
import com.sapo.mock_project.inventory_receipt.repositories.gin.GINProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.gin.GINRepository;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.user.UserRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.GINSpecification;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Triển khai các dịch vụ liên quan đến phiếu xuất kho (GIN).
 * Cung cấp các phương thức để tạo mới, lấy chi tiết, lọc, cập nhật và xóa phiếu xuất kho.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GINServiceImpl implements GINService {
    private final GINRepository ginRepository;
    private final GINProductRepository ginProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final GINMapper ginMapper;
    private final AuthHelper authHelper;
    private final LocalizationUtils localizationUtils;

    /**
     * Tạo mới một phiếu xuất kho.
     *
     * @param request Đối tượng chứa thông tin tạo mới phiếu xuất kho.
     * @return Phản hồi với kết quả của thao tác tạo mới.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createGIN(CreateGINRequest request) {
        try {
            // Kiểm tra xem ID có tồn tại không
            if (request.getSubId() != null && ginRepository.existsBySubIdAndTenantId(request.getSubId(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.GIN_SUB_ID_EXISTED));
            }
            User userInspection = userRepository.findByIdAndTenantId(request.getUserInspectionId(), authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_NOT_FOUND)));
            User userCreated = authHelper.getUser();

            // Tạo đối tượng GIN mới và lưu vào cơ sở dữ liệu
            GIN newGIN = ginMapper.mapToEntity(request);

            List<GINProduct> ginProducts = new ArrayList<>();
            for (CreateGINProductRequest productRequest : request.getProducts()) {
                Product product = productRepository.findByIdAndTenantId(productRequest.getProductId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                GINProduct ginProduct = ginMapper.mapToEntity(productRequest);
                ginProduct.setProduct(product);
                ginProduct.setGin(newGIN);

                ginProducts.add(ginProduct);
            }

            newGIN.setProducts(ginProducts);
            newGIN.setUserCreated(userCreated);
            newGIN.setUserInspection(userInspection);
            if (request.isBalance()) {
                newGIN.setUserBalanced(userCreated);
                newGIN.setStatus(GINStatus.BALANCED);
                newGIN.setBalancedAt(LocalDateTime.now());

                for (GINProduct ginProduct : newGIN.getProducts()) {
                    Product product = ginProduct.getProduct();
                    product.setQuantity(ginProduct.getActualStock());
                    productRepository.save(product);
                }
            } else {
                newGIN.setStatus(GINStatus.CHECKING);
            }

            ginRepository.save(newGIN);
            ginProductRepository.saveAll(newGIN.getProducts());

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy thông tin chi tiết của phiếu xuất kho theo ID.
     *
     * @param id ID của phiếu xuất kho cần lấy thông tin.
     * @return Phản hồi với thông tin chi tiết của phiếu xuất kho.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getGINById(String id) {
        try {
            GIN gin = ginRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GIN_NOT_FOUND)));

            GINDetailResponse response = ginMapper.mapToResponse(gin);

            List<GINProductDetailResponse> productDetailResponses = new ArrayList<>();
            for (GINProduct ginProduct : gin.getProducts()) {
                GINProductDetailResponse productDetailResponse = ginMapper.mapToResponse(ginProduct);

                productDetailResponse.setProductId(ginProduct.getProduct().getId());
                productDetailResponse.setProductSubId(ginProduct.getProduct().getSubId());
                productDetailResponse.setName(ginProduct.getProduct().getName());
                productDetailResponse.setImage(!ginProduct.getProduct().getImages().isEmpty()
                        ? ginProduct.getProduct().getImages().get(0)
                        : null);
                productDetailResponse.setRealQuantity(ginProduct.getProduct().getQuantity());

                productDetailResponses.add(productDetailResponse);
            }

            response.setProducts(productDetailResponses);
            response.setUserCreatedName(gin.getUserCreated() != null ? gin.getUserCreated().getFullName() : null);
            response.setUserBalancedName(gin.getUserBalanced() != null ? gin.getUserBalanced().getFullName() : null);
            response.setUserInspectionName(gin.getUserInspection() != null ? gin.getUserInspection().getFullName() : null);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_GET_DETAIL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lọc danh sách phiếu xuất kho theo các tiêu chí.
     *
     * @param request      Đối tượng chứa các tiêu chí lọc.
     * @param filterParams Các trường cần ánh xạ và lọc.
     * @param page         Trang hiện tại.
     * @param size         Số lượng bản ghi trên mỗi trang.
     * @return Phản hồi với danh sách phiếu xuất kho đã lọc.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> filterGIN(GetListGINRequest request, Map<String, Boolean> filterParams, int page, int size) {
        try {
            GINSpecification ginSpecification = new GINSpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<GIN> ginPage = ginRepository.findAll(ginSpecification, pageable);

            // Ánh xạ các đối tượng GIN thành GINGetListResponse
            List<GINGetListResponse> responseList = ginPage.getContent().stream().map(gin -> {
                GINGetListResponse response = new GINGetListResponse();

                // Duyệt qua các entry trong filterParams
                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
                    String field = entry.getKey();
                    Boolean includeField = entry.getValue();

                    // Ánh xạ các trường dựa trên filterParams
                    if (includeField) {
                        if (field.equals("user_created_name")) {
                            response.setUserCreatedName(gin.getUserCreated() != null ? gin.getUserCreated().getFullName() : null);
                        } else if (field.equals("user_balanced_name")) {
                            response.setUserBalancedName(gin.getUserBalanced() != null ? gin.getUserBalanced().getFullName() : null);
                        } else if (field.equals("user_inspection_name")) {
                            response.setUserInspectionName(gin.getUserInspection() != null ? gin.getUserInspection().getFullName() : null);
                        } else {
                            try {
                                // Lấy giá trị của trường từ đối tượng supplier
                                Object fieldValue = CommonUtils.getFieldValue(gin, field);

                                // Nếu giá trị không null, tiếp tục xử lý
                                if (fieldValue != null) {
                                    // Kiểm tra kiểu dữ liệu của trường cần set
                                    Field responseField = CommonUtils.getFieldFromClassHierarchy(response.getClass(), StringUtils.snakeCaseToEqualCamelCase(field));
                                    responseField.setAccessible(true); // Cho phép truy cập vào trường private

                                    // Kiểm tra và chuyển đổi kiểu dữ liệu nếu cần
                                    Object convertedValue = CommonUtils.convertValueForField(responseField, fieldValue);

                                    // Đặt giá trị cho trường trong đối tượng response
                                    responseField.set(response, convertedValue);
                                }
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                // Xử lý lỗi nếu không thể lấy hoặc set giá trị cho trường
                                throw new RuntimeException("Failed to map field " + field + " using reflection", e);
                            }
                        }
                    }
                }

                return response;
            }).toList();

            Pagination<Object> responsePageable = Pagination.<Object>builder()
                    .data(responseList)
                    .totalItems(ginPage.getTotalElements())
                    .totalPage(ginPage.getTotalPages())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_GET_ALL_SUCCESSFULLY), responsePageable);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin của phiếu xuất kho theo ID.
     *
     * @param id      ID của phiếu xuất kho cần cập nhật.
     * @param request Đối tượng chứa thông tin cập nhật.
     * @return Phản hồi với kết quả của thao tác cập nhật.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateGIN(String id, UpdateGINRequest request) {
        try {
            GIN existingGIN = ginRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GIN_NOT_FOUND)));

            if (existingGIN.getStatus() == GINStatus.BALANCED) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageValidateKeys.GIN_BALANCED_CANNOT_DELETE));
            }
            List<GINProduct> existingProducts = new ArrayList<>(existingGIN.getProducts());

            // Cập nhật thông tin của phiếu xuất kho
            ginMapper.updateFromDTO(request, existingGIN);
            if (request.isBalance()) {
                existingGIN.setStatus(GINStatus.BALANCED);
            }

            List<GINProduct> updatedProducts = new ArrayList<>();
            for (UpdateGINProductRequest updateGINRequest : request.getProducts()) {
                Product product = productRepository.findByIdAndTenantId(updateGINRequest.getProductId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                if (request.isBalance()) {
                    product.setQuantity(updateGINRequest.getActualStock());
                    productRepository.save(product);
                }

                GINProduct ginProduct = ginMapper.mapToEntity(updateGINRequest);
                ginProduct.setProduct(product);
                ginProduct.setGin(existingGIN);


                for (GINProduct existingProduct : existingProducts) {
                    if (existingProduct.getProduct().getId().equals(updateGINRequest.getProductId())) {
                        ginProduct.setId(existingProduct.getId());
                        ginProduct.setSubId(existingProduct.getSubId());
                        break;
                    }
                }

                updatedProducts.add(ginProduct);
            }

            List<GINProduct> productsToDelete = existingProducts.stream()
                    .filter(existingProduct -> updatedProducts.stream()
                            .noneMatch(updatedProduct -> updatedProduct.getProduct().getId().equals(existingProduct.getProduct().getId())))
                    .toList();

            ginProductRepository.deleteAll(productsToDelete);
            ginRepository.save(existingGIN);
            ginProductRepository.saveAll(updatedProducts);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Xóa một phiếu xuất kho theo ID.
     *
     * @param id ID của phiếu xuất kho cần xóa.
     * @return Phản hồi với kết quả của thao tác
     * xóa.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> deleteGIN(String id) {
        try {
            GIN existingGIN = ginRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GIN_NOT_FOUND)));

            if (existingGIN.getStatus() == GINStatus.BALANCED) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageValidateKeys.GIN_BALANCED_CANNOT_DELETE));
            }

            // Đánh dấu phiếu xuất kho là đã xóa
            existingGIN.setStatus(GINStatus.DELETED);
            ginRepository.save(existingGIN);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_DELETE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cân đối phiếu xuất kho theo ID.
     *
     * @param id ID của phiếu xuất kho cần cân đối.
     * @return Phản hồi với kết quả của thao tác cân đối.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> balanceGIN(String id) {
        try {
            GIN existingGIN = ginRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GIN_NOT_FOUND)));

            User userBalanced = authHelper.getUser();

            for (GINProduct ginProduct : existingGIN.getProducts()) {
                Product product = ginProduct.getProduct();
                product.setQuantity(ginProduct.getActualStock());
                productRepository.save(product);
            }

            existingGIN.setUserBalanced(userBalanced);
            existingGIN.setBalancedAt(LocalDateTime.now());
            existingGIN.setStatus(GINStatus.BALANCED);

            ginRepository.save(existingGIN);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_BALANCE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> exportData(GetListGINRequest request, String mode) {
        try {
            GINSpecification ginSpecification = new GINSpecification(request, authHelper.getUser().getTenantId());
            List<GIN> gins = new ArrayList<>();
            if (mode.equals("DEFAULT")) {
                gins = ginRepository.findALlByTenantId(authHelper.getUser().getTenantId());
            } else if (mode.equals("FILTER")) {
                gins = ginRepository.findAll(ginSpecification);
            }

            List<ExportDataResponse> responses = gins.stream().map(gin -> {
                ExportDataResponse response = ginMapper.mapToExportResponse(gin);

                if (gin.getUserCreated() != null) {
                    response.setUserCreatedName(gin.getUserCreated().getFullName());
                }
                if (gin.getUserBalanced() != null) {
                    response.setUserBalancedName(gin.getUserBalanced().getFullName());
                }
                if (gin.getUserInspection() != null) {
                    response.setUserInspectionName(gin.getUserInspection().getFullName());
                }

                gin.getProducts().forEach(ginProduct -> {
                    response.getProducts().forEach(detail -> {
                        detail.setRealQuantity(detail.getActualStock().subtract(detail.getDiscrepancyQuantity()));
                        if (detail.getId().equals(ginProduct.getId())) {
                            detail.setProductSubId(ginProduct.getProduct().getSubId());
                            detail.setProductName(ginProduct.getProduct().getName());
                        }
                    });
                });

                return response;
            }).toList();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_GET_ALL_SUCCESSFULLY), responses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
