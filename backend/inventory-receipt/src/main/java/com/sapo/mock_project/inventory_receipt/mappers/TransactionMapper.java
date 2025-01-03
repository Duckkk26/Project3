package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.internal.transaction.AutoCreateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.CreateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.UpdateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.transaction.GetTotalTransactionResponse;
import com.sapo.mock_project.inventory_receipt.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    Transaction mapToEntity(CreateTransactionRequest request);

    Transaction mapToEntity(AutoCreateTransactionRequest request);

    void updateFromToDTO(UpdateTransactionRequest request, @MappingTarget Transaction transaction);

    GetTotalTransactionResponse mapToTotalResponse(Transaction transaction);
}
