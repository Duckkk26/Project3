package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.brand.CreateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.brand.UpdateBrandRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.brand.BrandDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.brand.BrandGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BrandMapper {
    Brand mapToEntity(CreateBrandRequest request);

    void updateFromDTO(UpdateBrandRequest request, @MappingTarget Brand brand);

    BrandDetailResponse mapToResponse(Brand brand);

    BrandGetListResponse mapToResponseList(Brand brand);
}
