package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.product.CreateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.QuickCreateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.UpdateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.product.ProductDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.product.QuickGetListProductResponse;
import com.sapo.mock_project.inventory_receipt.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    Product mapToEntity(QuickCreateProductRequest product);

    Product mapToEntity(CreateProductRequest product);

    ProductDetailResponse mapToResponse(Product product);

    void updateFromDTO(UpdateProductRequest product, @MappingTarget Product entity);

    QuickGetListProductResponse mapToQuickGetListProductResponse(Product product);
}
