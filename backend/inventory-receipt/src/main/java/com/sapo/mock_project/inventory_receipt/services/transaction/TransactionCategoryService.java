package com.sapo.mock_project.inventory_receipt.services.transaction;

import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.CreateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.GetListTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.UpdateTransactionCategoryRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface TransactionCategoryService {
    ResponseEntity<ResponseObject<Object>> createTransactionCategory(CreateTransactionCategoryRequest request);

    ResponseEntity<ResponseObject<Object>> getListTransactionCategory(GetListTransactionCategoryRequest request, int page, int size);

    ResponseEntity<ResponseObject<Object>> updateTransactionCategory(String transactionCategoryId, UpdateTransactionCategoryRequest request);
}
