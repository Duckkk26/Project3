package com.sapo.mock_project.inventory_receipt.services.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.ExportDataOrderResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderGetListResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderResponse;
import com.sapo.mock_project.inventory_receipt.entities.*;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.OrderMapper;
import com.sapo.mock_project.inventory_receipt.repositories.order.OrderDetailRepository;
import com.sapo.mock_project.inventory_receipt.repositories.order.OrderRepository;
import com.sapo.mock_project.inventory_receipt.repositories.order.OrderRepositoryCustom;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.OrderSpecification;
import com.sapo.mock_project.inventory_receipt.services.supplier.SupplierService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final SupplierRepository supplierRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> createOrder(CreateOrderRequest request) {
        try {
            if (request.getSubId() != null && orderRepository.existsBySubIdAndTenantId(request.getSubId(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.ORDER_SUB_ID_EXISTED));
            }
            Supplier exstingSupplier = supplierRepository.findByIdAndTenantId(request.getSupplierId(), authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND)));

            User userCreated = authHelper.getUser();

            Order newOrder = orderMapper.mapToEntity(request);

            List<OrderDetail> newOrderDetail = new ArrayList<>();
            for (CreateOrderDetailRequest detailRequest : request.getProducts()) {
                Product exstingProduct = productRepository.findByIdAndTenantId(detailRequest.getProductId(), authHelper.getUser().getTenantId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                OrderDetail orderDetail = orderMapper.mapToEntityDetail(detailRequest);

                orderDetail.setOrder(newOrder);
                orderDetail.setProduct(exstingProduct);

                newOrderDetail.add(orderDetail);
            }

            newOrder.setSupplier(exstingSupplier);
            newOrder.setUserCreated(userCreated);
            newOrder.setOrderDetails(newOrderDetail);
            newOrder.setStatus(OrderStatus.PENDING);
            newOrder.setExpectedAt(DateUtils.getDateTimeTo(request.getExpectedAt()));

            newOrder.initializeOrder();
            newOrder.calculateTotalPrice();

            orderRepository.save(newOrder);
            orderDetailRepository.saveAll(newOrderDetail);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> filterOrder(GetListOrderRequest request,
                                                              Map<String, Boolean> filterParams,
                                                              int page, int size) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filterJson = objectMapper.writeValueAsString(filterParams);

            List<OrderGetListResponse> orderGetListResponses = orderRepositoryCustom.getFilteredOrders(
                    filterJson,
                    request.getKeyword(),
                    CommonUtils.joinParams(request.getStatuses()),
                    CommonUtils.joinParams(request.getSupplierIds()),
                    request.getStartCreatedAt(),
                    request.getEndCreatedAt(),
                    request.getStartExpectedAt(),
                    request.getEndExpectedAt(),
                    CommonUtils.joinParams(request.getProductIds()),
                    CommonUtils.joinParams(request.getUserCreatedIds()),
                    CommonUtils.joinParams(request.getUserCompletedIds()),
                    CommonUtils.joinParams(request.getUserCancelledIds()),
                    authHelper.getUser().getTenantId(),
                    page,
                    size
            );

            int totalOrders = orderRepositoryCustom.countTotalOrders(
                    filterJson,
                    request.getKeyword(),
                    CommonUtils.joinParams(request.getStatuses()),
                    CommonUtils.joinParams(request.getSupplierIds()),
                    request.getStartCreatedAt(),
                    request.getEndCreatedAt(),
                    request.getStartExpectedAt(),
                    request.getEndExpectedAt(),
                    CommonUtils.joinParams(request.getProductIds()),
                    CommonUtils.joinParams(request.getUserCreatedIds()),
                    CommonUtils.joinParams(request.getUserCompletedIds()),
                    CommonUtils.joinParams(request.getUserCancelledIds()),
                    authHelper.getUser().getTenantId()
            );

            int totalPages = (int) Math.ceil((double) totalOrders / size);

            Pagination pagination = Pagination.<Object>builder()
                    .data(orderGetListResponses)
                    .totalPage(totalPages)
                    .totalItems(totalOrders)
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getOrderById(String id) {
        try {
            Optional<Order> order = orderRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId());
            if (order.isEmpty()) {
                return ResponseUtil.error404Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.ORDER_NOT_FOUND));
            }
            Order existingOrder = order.get();

            OrderResponse orderResponse = orderMapper.mapToResponse(existingOrder);
            orderResponse.setSupplierId(existingOrder.getSupplier().getId());
            orderResponse.setUserCreatedName(existingOrder.getUserCreated().getFullName());
            for (OrderDetailResponse orderDetail : orderResponse.getOrderDetails()) {
                Optional<Map<String, Object>> optionalDataMap = existingOrder.getOrderDetails().stream()
                        .filter(detail -> detail.getSubId().equals(orderDetail.getSubId()))
                        .map(detail -> Map.of(
                                "productName", detail.getProduct().getName(),
                                "productId", detail.getProduct().getId(),
                                "productImage", detail.getProduct().getImages() == null
                                                || detail.getProduct().getImages().isEmpty()
                                                    ? new ProductImage()
                                                    : detail.getProduct().getImages().get(0)
                        ))
                        .findFirst();

                Map<String, Object> dataMap = optionalDataMap.get();

                orderDetail.setProductName((String) dataMap.get("productName"));
                orderDetail.setProductId((String) dataMap.get("productId"));
                orderDetail.setProductImage(dataMap.get("productImage") != null
                        ? (ProductImage) dataMap.get("productImage")
                        : null);
            }

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_GET_DETAIL_SUCCESSFULLY), orderResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> updateOrder(String orderId, UpdateOrderRequest request) {
        try {
            Optional<Order> orderOptional = orderRepository.findByIdAndTenantId(orderId, authHelper.getUser().getTenantId());
            if (orderOptional.isEmpty()) {
                return ResponseUtil.error404Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.ORDER_NOT_FOUND));
            }

            Order existingOrder = orderOptional.get();
            orderMapper.updateFromDTO(request, existingOrder);

            List<OrderDetail> newOrderDetails = new ArrayList<>();
            for (CreateOrderDetailRequest detailRequest : request.getProducts()) {
                Optional<Product> productOptional = productRepository.findByIdAndTenantId(detailRequest.getProductId(), authHelper.getUser().getTenantId());
                if (productOptional.isEmpty()) {
                    return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND));
                }

                OrderDetail orderDetail = orderMapper.mapToEntityDetail(detailRequest);

                orderDetail.setOrder(existingOrder);
                orderDetail.setProduct(productOptional.get());

                for (OrderDetail existingOrderDetail : existingOrder.getOrderDetails()) {
                    if (existingOrderDetail.getProduct().getId().equals(orderDetail.getProduct().getId())) {
                        orderDetail.setId(existingOrderDetail.getId());
                        orderDetail.setSubId(existingOrderDetail.getSubId());
                        break;
                    }
                }

                newOrderDetails.add(orderDetail);
            }

            List<OrderDetail> deletedOrderDetails = existingOrder.getOrderDetails().stream()
                    .filter(orderDetail -> newOrderDetails.stream()
                            .noneMatch(newOrderDetail -> newOrderDetail.getProduct().getId().equals(orderDetail.getProduct().getId())))
                    .toList();

            existingOrder.setOrderDetails(newOrderDetails);

            existingOrder.calculateTotalPrice();

            orderDetailRepository.saveAll(newOrderDetails);
            orderRepository.save(existingOrder);
            orderDetailRepository.deleteAll(deletedOrderDetails);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> updateOrderLittle(String orderId, UpdateOrderLittleRequest request) {
        try {
            Optional<Order> orderOptional = orderRepository.findByIdAndTenantId(orderId, authHelper.getUser().getTenantId());
            if (orderOptional.isEmpty()) {
                return ResponseUtil.error404Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.ORDER_NOT_FOUND));
            }

            Order existingOrder = orderOptional.get();
            if (request.getNote() != null) {
                existingOrder.setNote(request.getNote());
            }
            if (request.getTags() != null) {
                existingOrder.setTags(request.getTags());
            }
            if (!request.getNoteDetails().isEmpty()) {
                for (Map<String, String> noteDetail : request.getNoteDetails()) {
                    Optional<OrderDetail> orderDetailOptional = existingOrder.getOrderDetails().stream()
                            .filter(detail -> detail.getId().equals(noteDetail.get("id")))
                            .findFirst();

                    if (orderDetailOptional.isEmpty()) {
                        return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageExceptionKeys.ORDER_NOT_FOUND));
                    }

                    OrderDetail existingOrderDetail = orderDetailOptional.get();
                    existingOrderDetail.setNote(noteDetail.get("note"));

                    orderDetailRepository.save(existingOrderDetail);
                }
            }

            orderRepository.save(existingOrder);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> exportData(GetListOrderRequest request, String mode) {
        try {
            OrderSpecification orderSpecification = new OrderSpecification(request, authHelper.getUser().getTenantId());
            List<Order> orders = new ArrayList<>();

            if (mode.equals("DEFAULT")) {
                orders = orderRepository.findAllByTenantId(authHelper.getUser().getTenantId());
            } else if (mode.equals("FILTER")) {
                orders = orderRepository.findAll(orderSpecification);
            }

            List<ExportDataOrderResponse> responses = orders.stream().map(order -> {
                ExportDataOrderResponse response = orderMapper.mapToExportDataResponse(order);
                response.setSupplierName(order.getSupplier().getName());
                response.setUserCreatedName(order.getUserCreated().getFullName());

                response.setTotalQuantity(order.getOrderDetails().stream().map(OrderDetail::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));

                return response;
            }).toList();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_GET_ALL_SUCCESSFULLY), responses);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> cancelOrder(String orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findByIdAndTenantId(orderId, authHelper.getUser().getTenantId());
            if (orderOptional.isEmpty()) {
                return ResponseUtil.error404Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.ORDER_NOT_FOUND));
            }

            Order existingOrder = orderOptional.get();
            existingOrder.setStatus(OrderStatus.CANCELLED);

            orderRepository.save(existingOrder);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CANCEL_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}