package com.sapo.mock_project.inventory_receipt.services.brand;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.CreateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.GetListBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.UpdateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.brand.BrandDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.Brand;
import com.sapo.mock_project.inventory_receipt.entities.User;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.BrandMapper;
import com.sapo.mock_project.inventory_receipt.repositories.brand.BrandRepository;
import com.sapo.mock_project.inventory_receipt.services.brand.BrandService;
import com.sapo.mock_project.inventory_receipt.services.specification.BrandSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    @Override
    public ResponseEntity<ResponseObject<Object>> createBrand(CreateBrandRequest request) {
        try {
            if (brandRepository.existsByNameAndTenantId(request.getName(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.BRAND_NAME_EXISTED));
            }

            Brand newBrand = brandMapper.mapToEntity(request);

            brandRepository.save(newBrand);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.BRAND_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getBrandById(String brandId) {
        try {
            Optional<Brand> brandOptional = brandRepository.findByIdAndTenantId(brandId, authHelper.getUser().getTenantId());
            if (brandOptional.isEmpty()) {
                throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.BRAND_NOT_FOUND));
            }

            Brand existingBrand = brandOptional.get();

            BrandDetailResponse brandDetailResponse = brandMapper.mapToResponse(existingBrand);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.BRAND_GET_DETAIL_SUCCESSFULLY), brandDetailResponse);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getAllBrands(GetListBrandRequest request, int page, int size) {
        try {
            BrandSpecification brandSpecification = new BrandSpecification(request, authHelper.getUser().getTenantId());
            // Sắp xếp theo tên nhà cung cấp tăng dần
            Sort sort = Sort.by(Sort.Direction.ASC, "name");
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<Brand> brandPage = brandRepository.findAll(brandSpecification, pageable);

            List<BrandDetailResponse> brandDetailResponses = brandPage.getContent().stream().map(brandMapper::mapToResponse).toList();

            Pagination pagination = Pagination.<Object>builder()
                    .data(brandDetailResponses)
                    .totalPage(brandPage.getTotalPages())
                    .totalItems(brandPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.BRAND_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> updateBrand(String brandId, UpdateBrandRequest request) {
        try {
            Optional<Brand> brandOptional = brandRepository.findByIdAndTenantId(brandId, authHelper.getUser().getTenantId());
            if (brandOptional.isEmpty()) {
                throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.BRAND_NOT_FOUND));
            }

            Brand existingBrand = brandOptional.get();

            if (brandRepository.existsByNameAndTenantId(request.getName(), authHelper.getUser().getTenantId()) && !existingBrand.getName().equals(request.getName())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.BRAND_NAME_EXISTED));
            }

            brandMapper.updateFromDTO(request, existingBrand);

            brandRepository.save(existingBrand);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.BRAND_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
