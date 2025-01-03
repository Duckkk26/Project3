package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.grn.GRNProductDetail;
import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GRNProductMapper {
    GRNProductDetail mapToResponse(GRNProduct grnProduct);

    GRNProduct mapToEntityProduct(CreateGRNProductRequest request);

//    void updateFromDTO(UpdateGRNRequest request, GRN existingGRN);
}
