package com.sapo.mock_project.inventory_receipt.services.refundinfor;

import com.sapo.mock_project.inventory_receipt.dtos.request.refund.CreateRefundInforRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface RefundInformationService {
    ResponseEntity<ResponseObject<Object>> createRefundInformation(CreateRefundInforRequest request);

    ResponseEntity<ResponseObject<Object>> getAllByGRN(String grnId);
}
