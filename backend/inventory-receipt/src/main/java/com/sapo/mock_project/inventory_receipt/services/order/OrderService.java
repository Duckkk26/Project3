package com.sapo.mock_project.inventory_receipt.services.order;

import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.GetListOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.UpdateOrderLittleRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.UpdateOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OrderService {
    ResponseEntity<ResponseObject<Object>> createOrder(CreateOrderRequest request);

    ResponseEntity<ResponseObject<Object>> filterOrder(GetListOrderRequest request,
                                                       Map<String, Boolean> filterParams,
                                                       int page, int size);

    ResponseEntity<ResponseObject<Object>> getOrderById(String id);

    ResponseEntity<ResponseObject<Object>> updateOrder(String orderId, UpdateOrderRequest request);

    ResponseEntity<ResponseObject<Object>> updateOrderLittle(String orderId, UpdateOrderLittleRequest request);

    ResponseEntity<ResponseObject<Object>> exportData(GetListOrderRequest request, String mode);

    ResponseEntity<ResponseObject<Object>> cancelOrder(String orderId);
}
