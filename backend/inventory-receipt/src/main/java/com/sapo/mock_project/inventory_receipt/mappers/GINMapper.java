package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.gin.CreateGINProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.CreateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.UpdateGINProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.UpdateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.ExportDataResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.GINDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.GINProductDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.GIN;
import com.sapo.mock_project.inventory_receipt.entities.GINProduct;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GINMapper {
    GIN mapToEntity(CreateGINRequest request);

    GINProduct mapToEntity(CreateGINProductRequest request);

    GINProduct mapToEntity(UpdateGINProductRequest request);

    GINDetailResponse mapToResponse(GIN gin);

    GINProductDetailResponse mapToResponse(GINProduct ginProduct);

    void updateFromDTO(UpdateGINRequest request, @MappingTarget GIN existingGIN);

    ExportDataResponse mapToExportResponse(GIN gin);
}
