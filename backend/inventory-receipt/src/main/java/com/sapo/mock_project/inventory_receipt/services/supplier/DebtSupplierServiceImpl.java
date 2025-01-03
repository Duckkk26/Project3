package com.sapo.mock_project.inventory_receipt.services.supplier;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListDebtSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.supplier.DebtSupplierGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.DebtSupplier;
import com.sapo.mock_project.inventory_receipt.mappers.SupplierMapper;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.DebtSupplierRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.DebtSupplierSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtSupplierServiceImpl implements DebtSupplierService {
    private final DebtSupplierRepository debtSupplierRepository;
    private final SupplierMapper supplierMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    @Override
    public ResponseEntity<ResponseObject<Object>> filterDebtSupplier(GetListDebtSupplierRequest request, int page, int size) {
        try {
            DebtSupplierSpecification specification = new DebtSupplierSpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt");

            Page<DebtSupplier> debtSupplierPage = debtSupplierRepository.findAll(specification, pageable);

            List<DebtSupplierGetListResponse> responses = debtSupplierPage.getContent().stream()
                    .map(debtSupplier -> {
                        DebtSupplierGetListResponse response = supplierMapper.mapToResponseDebtSupplier(debtSupplier);
                        response.setUserCreatedName(debtSupplier.getUserCreated().getFullName());

                        return response;
                    })
                    .toList();

            Pagination pagination = Pagination.<Object>builder()
                    .data(responses)
                    .totalPage(debtSupplierPage.getTotalPages())
                    .totalItems(debtSupplierPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.DEBT_SUPPLIER_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
