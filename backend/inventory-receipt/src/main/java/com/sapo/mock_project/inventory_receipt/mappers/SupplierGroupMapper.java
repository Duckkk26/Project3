package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.CreateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.UpdateSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.suppliergroup.SUPGGetAllResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.suppliergroup.SupplierDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.SupplierGroup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplierGroupMapper {
    SupplierGroup mapToEntity(CreateSupplierGroupRequest request);

    SupplierDetailResponse mapToResponse(SupplierGroup entity);

    SUPGGetAllResponse mapToGetAllResponse(SupplierGroup entity);

    void updateFromToDTO(UpdateSupplierGroupRequest request, @MappingTarget SupplierGroup entity);
}
