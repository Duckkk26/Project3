package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.category.CreateCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.category.CategoryGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    Category mapToEntity(CreateCategoryRequest request);

    CategoryGetListResponse mapToResponse(Category category);
}
