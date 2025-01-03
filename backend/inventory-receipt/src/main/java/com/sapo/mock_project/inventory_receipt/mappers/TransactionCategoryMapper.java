package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.CreateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.UpdateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.transaction.TransactionCategoryGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionCategoryMapper {
    TransactionCategory mapToEntity(CreateTransactionCategoryRequest request);

    TransactionCategoryGetListResponse mapToResponse(TransactionCategory entity);

    void updateFromToDTO(UpdateTransactionCategoryRequest request, @MappingTarget TransactionCategory entity);
}
