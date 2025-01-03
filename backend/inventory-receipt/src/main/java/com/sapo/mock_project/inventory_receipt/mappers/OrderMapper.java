package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderDetailRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.UpdateOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.ExportDataOrderResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderResponse;
import com.sapo.mock_project.inventory_receipt.entities.Order;
import com.sapo.mock_project.inventory_receipt.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    Order mapToEntity(CreateOrderRequest request);

    OrderDetail mapToEntityDetail(CreateOrderDetailRequest request);

    OrderResponse mapToResponse(Order order);

    OrderDetailResponse mapToResponseDetail(OrderDetail orderDetail);

    void updateFromDTO(UpdateOrderRequest request, @MappingTarget Order order);

    ExportDataOrderResponse mapToExportDataResponse(Order order);
}
