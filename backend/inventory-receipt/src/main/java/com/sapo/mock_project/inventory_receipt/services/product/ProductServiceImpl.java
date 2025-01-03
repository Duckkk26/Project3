package com.sapo.mock_project.inventory_receipt.services.product;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.product.ProductDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.product.ProductGetListResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.product.ProductManageGetListResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.product.QuickGetListProductResponse;
import com.sapo.mock_project.inventory_receipt.entities.Brand;
import com.sapo.mock_project.inventory_receipt.entities.Category;
import com.sapo.mock_project.inventory_receipt.entities.Product;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.ProductMapper;
import com.sapo.mock_project.inventory_receipt.repositories.brand.BrandRepository;
import com.sapo.mock_project.inventory_receipt.repositories.category.CategoryRepository;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.ProductManageSpecification;
import com.sapo.mock_project.inventory_receipt.services.specification.ProductSpecification;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    private final ProductMapper productMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    @Override
    public ResponseEntity<ResponseObject<Object>> quickCreateProduct(QuickCreateProductRequest request) {
        try {
            Product newProduct = productMapper.mapToEntity(request);

            if (request.getCategoryId() != null) {
                Category existingCategory = categoryRepository.findByIdAndTenantId(request.getCategoryId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.CATEGORY_NOT_FOUND)));

                newProduct.setCategory(existingCategory);
            }

            newProduct.calculatorValue();

            productRepository.save(newProduct);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_CREATED_SUCCESSFULLY), newProduct.getId());
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> createProduct(CreateProductRequest request) {
        try {
            Product newProduct = productMapper.mapToEntity(request);

            if (request.getCategoryId() != null) {
                Category existingCategory = categoryRepository.findByIdAndTenantId(request.getCategoryId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.CATEGORY_NOT_FOUND)));

                newProduct.setCategory(existingCategory);
            }

            if (request.getBrandId() != null) {
                Brand existingBrand = brandRepository.findByIdAndTenantId(request.getBrandId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.BRAND_NOT_FOUND)));

                newProduct.setBrand(existingBrand);
            }

            newProduct.calculatorValue();

            productRepository.save(newProduct);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_CREATED_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> updateProduct(String productId, UpdateProductRequest request) {
        try {
            Product existingProduct = productRepository.findByIdAndTenantId(productId, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

            productMapper.updateFromDTO(request, existingProduct);

            if (request.getCategoryId() != null) {
                Category existingCategory = categoryRepository.findByIdAndTenantId(request.getCategoryId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.CATEGORY_NOT_FOUND)));

                existingProduct.setCategory(existingCategory);
            }

            if (request.getBrandId() != null) {
                Brand existingBrand = brandRepository.findByIdAndTenantId(request.getBrandId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.BRAND_NOT_FOUND)));

                existingProduct.setBrand(existingBrand);
            }

            existingProduct.calculatorValue();

            productRepository.save(existingProduct);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_UPDATED_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> filterProduct(GetListProductRequest request, Map<String, Boolean> filterParams, int page, int size) {
        try {
            ProductSpecification specification = new ProductSpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<Product> productPage = productRepository.findAll(specification, pageable);

            List<ProductGetListResponse> productGetListResponses = productPage.getContent().stream().map(product -> {
                ProductGetListResponse response = new ProductGetListResponse();

                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
                    String field = entry.getKey();
                    Boolean includeField = entry.getValue();

                    if (includeField) {
                        if (field.equals("category_name")) {
                            if (product.getCategory() != null) {
                                response.setCategoryName(product.getCategory().getName());
                            }
                        } else if (field.equals("brand_name")) {
                            if (product.getBrand() != null) {
                                response.setBrandName(product.getBrand().getName());
                            }
                        } else {
                            try {
                                // Lấy giá trị của trường từ đối tượng supplier
                                Object fieldValue = CommonUtils.getFieldValue(product, field);

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

            Pagination pagination = Pagination.<Object>builder()
                    .data(productGetListResponses)
                    .totalPage(productPage.getTotalPages())
                    .totalItems(productPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> filterProductInWarehouse(GetListProductManageRequest request, Map<String, Boolean> filterParams, int page, int size) {
        try {
            ProductManageSpecification specification = new ProductManageSpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<Product> productPage = productRepository.findAll(specification, pageable);

            List<ProductManageGetListResponse> listResponses = productPage.getContent().stream().map(product -> {
                ProductManageGetListResponse response = new ProductManageGetListResponse();

                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
                    String field = entry.getKey();
                    Boolean includeField = entry.getValue();

                    if (includeField) {
                        if (field.equals("category_name")) {
                            if (product.getCategory() != null) {
                                response.setCategoryName(product.getCategory().getName());
                            }
                        } else if (field.equals("brand_name")) {
                            if (product.getBrand() != null) {
                                response.setBrandName(product.getBrand().getName());
                            }
                        } else if (field.equals("incoming_quantity")) {
                            List<Object[]> incomingQuantity = productRepository.getIncomingQuantity(product.getId());
                            if (incomingQuantity != null && !incomingQuantity.isEmpty()) {
                                response.setIncomingQuantity((BigDecimal) incomingQuantity.get(0)[0]);
                            }
                        } else {
                            try {
                                // Lấy giá trị của trường từ đối tượng supplier
                                Object fieldValue = CommonUtils.getFieldValue(product, field);

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

            Pagination pagination = Pagination.<Object>builder()
                    .data(listResponses)
                    .totalPage(productPage.getTotalPages())
                    .totalItems(productPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> quickGetListProduct(String keyword, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "name"));

            Page<Product> productPage = productRepository.quickGetListProduct(keyword, authHelper.getUser().getTenantId(), pageable);

            List<QuickGetListProductResponse> listResponses = productPage.getContent().stream().map(productMapper::mapToQuickGetListProductResponse).toList();

            Pagination pagination = Pagination.<Object>builder()
                    .data(listResponses)
                    .totalPage(productPage.getTotalPages())
                    .totalItems(productPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> deleteProduct(String productId) {
        try {
            Product existingProduct = productRepository.findByIdAndTenantId(productId, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));
            existingProduct.setStatus(ProductStatus.INACTIVE);

            productRepository.save(existingProduct);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_DELETED_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getDetailProduct(String productId) {
        try {
            Product existingProduct = productRepository.findByIdAndTenantId(productId, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

            ProductDetailResponse response = productMapper.mapToResponse(existingProduct);
            if (existingProduct.getCategory() != null) {
                response.setCategoryId(existingProduct.getCategory().getId());
                response.setCategoryName(existingProduct.getCategory().getName());
            }
            if (existingProduct.getBrand() != null) {
                response.setBrandId(existingProduct.getBrand().getId());
                response.setBrandName(existingProduct.getBrand().getName());
            }
            if (existingProduct.getImages() != null && !existingProduct.getImages().isEmpty()) {
                response.setImage(existingProduct.getImages().get(0));
            }


            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_GET_DETAIL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
