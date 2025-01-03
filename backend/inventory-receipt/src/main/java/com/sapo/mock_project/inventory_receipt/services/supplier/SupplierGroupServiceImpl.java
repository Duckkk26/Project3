package com.sapo.mock_project.inventory_receipt.services.supplier;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierGroupStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.CreateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.GetListSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.UpdateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.suppliergroup.SUPGGetAllResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.suppliergroup.SupplierDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.SupplierGroup;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.SupplierGroupMapper;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierGroupRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.SupplierGroupSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Lớp SupplierGroupServiceImpl chịu trách nhiệm xử lý các nghiệp vụ liên quan đến nhóm nhà cung cấp.
 * Cung cấp các chức năng như tạo, lấy thông tin, cập nhật và xóa nhóm nhà cung cấp.
 */
@Service
@RequiredArgsConstructor
public class SupplierGroupServiceImpl implements SupplierGroupService {
    private final SupplierGroupRepository supplierGroupRepository;
    private final SupplierGroupMapper supplierGroupMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    /**
     * Tạo mới một nhóm nhà cung cấp.
     *
     * @param request yêu cầu chứa thông tin của nhóm nhà cung cấp.
     * @return ResponseEntity chứa kết quả của việc tạo nhóm nhà cung cấp.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createSupplierGroup(CreateSupplierGroupRequest request) {
        try {
            // Kiểm tra xem tên nhóm nhà cung cấp đã tồn tại hay chưa
            if (supplierGroupRepository.existsByNameAndTenantId(request.getName(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_GROUP_NAME_EXISTED));
            }
            // Kiểm tra xem ID nhóm nhà cung cấp đã tồn tại hay chưa
            if (request.getSubId() != null && supplierGroupRepository.existsBySubIdAndTenantId(request.getSubId(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_GROUP_ID_EXISTED));
            }

            // Tạo mới nhóm nhà cung cấp
            SupplierGroup newSupplierGroup = supplierGroupMapper.mapToEntity(request);
            newSupplierGroup.setStatus(SupplierGroupStatus.ACTIVE);

            // Lưu nhóm nhà cung cấp vào cơ sở dữ liệu
            supplierGroupRepository.save(newSupplierGroup);

            // Trả về kết quả thành công
            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_GROUP_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy thông tin chi tiết của một nhóm nhà cung cấp theo ID.
     *
     * @param supplierGroupId ID của nhóm nhà cung cấp.
     * @return ResponseEntity chứa thông tin chi tiết của nhóm nhà cung cấp.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getSupplierGroupById(String supplierGroupId) {
        try {
            // Tìm nhóm nhà cung cấp theo ID
            Optional<SupplierGroup> supplierGroup = supplierGroupRepository.findByIdAndTenantId(supplierGroupId, authHelper.getUser().getTenantId());
            if (supplierGroup.isEmpty()) {
                // Ném ngoại lệ nếu không tìm thấy
                throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_GROUP_NOT_FOUND));
            }

            SupplierGroup existingSupplierGroup = supplierGroup.get();

            // Chuyển đổi dữ liệu nhóm nhà cung cấp sang response
            SupplierDetailResponse response = supplierGroupMapper.mapToResponse(existingSupplierGroup);

            // Trả về kết quả thành công với thông tin chi tiết
            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_GROUP_GET_DETAIL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy danh sách tất cả các nhóm nhà cung cấp với phân trang.
     *
     * @param page số trang.
     * @param size số lượng bản ghi mỗi trang.
     * @return ResponseEntity chứa danh sách các nhóm nhà cung cấp.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getAllSupplierGroup(GetListSupplierGroupRequest request, int page, int size) {
        try {
            SupplierGroupSpecification supplierGroupSpecification = new SupplierGroupSpecification(request, authHelper.getUser().getTenantId());
            // Sắp xếp theo tên nhà cung cấp tăng dần
            Sort sort = Sort.by(Sort.Direction.ASC, "name");
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            // Lấy danh sách nhóm nhà cung cấp theo trạng thái ACTIVE
            Page<SupplierGroup> supplierGroups = supplierGroupRepository.findAll(supplierGroupSpecification, pageable);

            // Chuyển đổi dữ liệu nhóm nhà cung cấp sang response
            Page<SUPGGetAllResponse> supplierGroupResponses = supplierGroups.map(supplierGroup -> {
                SUPGGetAllResponse response = supplierGroupMapper.mapToGetAllResponse(supplierGroup);
                response.setTotalSupplier(supplierGroup.getSuppliers() != null ? supplierGroup.getSuppliers().size() : 0);
                return response;
            });

            // Tạo đối tượng phân trang cho kết quả trả về
            Pagination<Object> response = Pagination.<Object>builder()
                    .data(supplierGroupResponses.getContent())
                    .totalItems(supplierGroupResponses.getTotalElements())
                    .totalPage(supplierGroupResponses.getTotalPages())
                    .build();

            // Trả về kết quả thành công
            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_GROUP_GET_ALL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin nhóm nhà cung cấp theo ID.
     *
     * @param supplierGroupId ID của nhóm nhà cung cấp cần cập nhật.
     * @param request         yêu cầu chứa thông tin cần cập nhật.
     * @return ResponseEntity chứa kết quả cập nhật nhóm nhà cung cấp.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateSupplierGroup(String supplierGroupId, UpdateSupplierGroupRequest request) {
        try {
            // Tìm nhóm nhà cung cấp theo ID
            SupplierGroup existingSupplierGroup = supplierGroupRepository.findByIdAndTenantId(supplierGroupId, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_GROUP_NOT_FOUND)));

            // Kiểm tra tên nhóm nhà cung cấp có bị trùng không
            if (!existingSupplierGroup.getName().equals(request.getName())
                && supplierGroupRepository.existsByNameAndTenantId(request.getName(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_GROUP_NAME_EXISTED));
            }
            // Kiểm tra ID nhóm nhà cung cấp có bị trùng không
            if (request.getSubId() != null
                    && !existingSupplierGroup.getId().equals(request.getSubId())
                        && supplierGroupRepository.existsBySubIdAndTenantId(request.getSubId(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_GROUP_ID_EXISTED));
            }

            // Cập nhật thông tin nhóm nhà cung cấp
            supplierGroupMapper.updateFromToDTO(request, existingSupplierGroup);

            // Lưu thông tin đã cập nhật vào cơ sở dữ liệu
            supplierGroupRepository.save(existingSupplierGroup);

            // Trả về kết quả thành công
            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_GROUP_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Xóa (chuyển trạng thái) một nhóm nhà cung cấp sang INACTIVE.
     *
     * @param supplierGroupId ID của nhóm nhà cung cấp cần xóa.
     * @return ResponseEntity chứa kết quả xóa nhóm nhà cung cấp.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> deleteSupplierGroup(String supplierGroupId) {
        try {
            // Tìm nhóm nhà cung cấp theo ID
            SupplierGroup existingSupplierGroup = supplierGroupRepository.findByIdAndTenantId(supplierGroupId, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_GROUP_NOT_FOUND)));

            // Cập nhật trạng thái nhóm nhà cung cấp thành INACTIVE
            existingSupplierGroup.setStatus(SupplierGroupStatus.INACTIVE);

            // Lưu thông tin đã cập nhật vào cơ sở dữ liệu
            supplierGroupRepository.save(existingSupplierGroup);

            // Trả về kết quả thành công
            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_GROUP_DELETE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
