package com.sapo.mock_project.inventory_receipt.services.transaction;

import com.sapo.mock_project.inventory_receipt.dtos.internal.transaction.AutoCreateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TransactionService {
    ResponseEntity<ResponseObject<Object>> createTransaction(CreateTransactionRequest request);

    ResponseEntity<ResponseObject<Object>> autoCreateTransaction(AutoCreateTransactionRequest request);

    ResponseEntity<ResponseObject<Object>> filterTransaction(GetListTransactionRequest request,
                                                             Map<String, Boolean> filterParams,
                                                             String sort, String sortField,
                                                             int page, int size);

    ResponseEntity<ResponseObject<Object>> updateTransaction(String id, UpdateTransactionRequest request);

    ResponseEntity<ResponseObject<Object>> cancelTransaction(String id);

    ResponseEntity<ResponseObject<Object>> getTransactionByRefundId(String refundId);

    ResponseEntity<ResponseObject<Object>> getTotalTransaction(GetTotalRequest request, int page, int size);

    ResponseEntity<ResponseObject<Object>> paymentGRN(CreateTransactionGRNRequest request);
}
