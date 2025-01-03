package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.refund.CreateRefundInforRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.refundinfor.RefundInformationService;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.REFUND_INFORMATION)
@RequiredArgsConstructor
public class RefundInformationController {
    private final RefundInformationService refundInformationService;

    @PostMapping(value = "/create.json")
    public ResponseEntity<ResponseObject<Object>> createRefundInformation(@Valid @RequestBody CreateRefundInforRequest request) {
        StringUtils.trimAllStringFields(request);

        return refundInformationService.createRefundInformation(request);
    }

    @GetMapping(value = "/all.json/{grnId}")
    public ResponseEntity<ResponseObject<Object>> getAllByGRN(@PathVariable String grnId) {
        return refundInformationService.getAllByGRN(grnId);
    }
}
