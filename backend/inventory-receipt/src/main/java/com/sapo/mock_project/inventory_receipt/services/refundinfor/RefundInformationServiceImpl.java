package com.sapo.mock_project.inventory_receipt.services.refundinfor;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundInformationStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.dtos.internal.transaction.AutoCreateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.refund.CreateRefundInforDetailRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.refund.CreateRefundInforRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.refund.CreateRefundTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.refundinformation.GetListByGRNResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.refundinformation.RefundDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.*;
import com.sapo.mock_project.inventory_receipt.mappers.RefundMapper;
import com.sapo.mock_project.inventory_receipt.repositories.grn.GRNRepository;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.refundinfor.RefundDetailRepository;
import com.sapo.mock_project.inventory_receipt.repositories.refundinfor.RefundInformationRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.DebtSupplierRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierRepository;
import com.sapo.mock_project.inventory_receipt.services.transaction.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefundInformationServiceImpl implements RefundInformationService {
    private final RefundInformationRepository refundInformationRepository;
    private final RefundDetailRepository refundDetailRepository;
    private final GRNRepository grnRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final DebtSupplierRepository debtSupplierRepository;

    private final TransactionService transactionService;

    private final RefundMapper refundMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> createRefundInformation(CreateRefundInforRequest request) {
        try {
            User userCreated = authHelper.getUser();

            Optional<GRN> grnOptional = grnRepository.findByIdAndTenantId(request.getGrnId(), authHelper.getUser().getTenantId());
            if (grnOptional.isEmpty()) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_NOT_FOUND));
            }

            GRN existingGRN = grnOptional.get();
            Supplier existingSupplier = existingGRN.getSupplier();

            RefundInformation newRefund = RefundInformation.builder()
                    .supplierId(existingSupplier.getId())
                    .supplierName(existingSupplier.getName())
                    .totalRefundedTax(request.getTotalTax())
                    .totalRefundedDiscount(request.getTotalDiscount())
                    .totalRefundedCost(request.getTotalCost())
                    .reason(request.getReason())
                    .status(RefundInformationStatus.REFUNDED)
                    .grn(existingGRN)
                    .userCreated(userCreated)
                    .build();

            List<RefundInformationDetail> refundDetails = new ArrayList<>();
            for (CreateRefundInforDetailRequest detailRequest : request.getRefundDetail()) {
//                boolean isExist = existingGRN.getGrnProducts().stream()
//                        .anyMatch(detail -> detail.getId().equals(detailRequest.getProductId()));
//                if (!isExist) {
//                    return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_IN_LIST));
//                }

                Optional<GRNProduct> optionalGRNProduct = existingGRN.getGrnProducts().stream()
                        .filter(grnProduct -> grnProduct.getId().equals(detailRequest.getProductId()))
                        .findFirst();
                if (optionalGRNProduct.isEmpty()) {
                    return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND));
                }
                GRNProduct existingGRNProduct = optionalGRNProduct.get();
//                Optional<Product> optionalProduct = productRepository.findById(detailRequest.getProductId());
//                if (optionalProduct.isEmpty()) {
//                    return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND));
//                }

                Product existingProduct = existingGRNProduct.getProduct();

                RefundInformationDetail newDetail = RefundInformationDetail.builder()
                        .quantity(detailRequest.getQuantity())
                        .refundedPrice(detailRequest.getRefundedPrice())
                        .amount(detailRequest.getAmount())
                        .product(existingProduct)
                        .refundInformation(newRefund)
                        .build();

                refundDetails.add(newDetail);

                existingProduct.setQuantity(existingProduct.getQuantity().subtract(newDetail.getQuantity()));
                productRepository.save(existingProduct);
            }

            newRefund.setRefundInformationDetails(refundDetails);
            newRefund.calculator();

            existingSupplier.setCurrentDebt(existingSupplier.getCurrentDebt().subtract(newRefund.getTotalRefundedValue()));

            List<DebtSupplier> debtSuppliers = new ArrayList<>();
            DebtSupplier debtSupplier = DebtSupplier.builder()
                    .amount(BigDecimal.ZERO.subtract(newRefund.getTotalRefundedValue()))
                    .debtAfter(existingSupplier.getCurrentDebt())
                    .referenceCode(PrefixId.GRN)
                    .referenceId(existingGRN.getId())
                    .note("Tạo đơn trả hàng")
                    .userCreated(userCreated)
                    .supplier(existingSupplier)
                    .build();
            debtSuppliers.add(debtSupplier);

            if (request.getTransaction() != null
                && request.getTransaction().getAmount() != null
                && request.getTransaction().getAmount().compareTo(BigDecimal.ZERO) != 0) {
                CreateRefundTransactionRequest transactionRequest = request.getTransaction();

                AutoCreateTransactionRequest autoCreateTransactionRequest = AutoCreateTransactionRequest.builder()
                        .amount(transactionRequest.getAmount())
                        .note("Phiếu thu tự động tạo khi nhà cung cấp hoàn tiền")
                        .referenceCode(PrefixId.REFUND_INFORMATION)
                        .referenceId(existingGRN.getId())
                        .recipientGroup(PrefixId.SUPPLIER)
                        .recipientId(existingSupplier.getId())
                        .recipientName(existingSupplier.getName())
                        .type(TransactionType.INCOME)
                        .paymentMethod(transactionRequest.getPaymentMethod())
                        .build();

                String transactionId = transactionService.autoCreateTransaction(autoCreateTransactionRequest)
                        .getBody().getData().toString();

                existingSupplier.setCurrentDebt(existingSupplier.getCurrentDebt().add(transactionRequest.getAmount()));

                DebtSupplier debtSupplierTransaction = DebtSupplier.builder()
                        .amount(transactionRequest.getAmount())
                        .debtAfter(existingSupplier.getCurrentDebt())
                        .referenceCode(PrefixId.TRANSACTION)
                        .referenceId(transactionId)
                        .note("Tạo phiếu thu")
                        .userCreated(userCreated)
                        .supplier(existingSupplier)
                        .build();

                debtSuppliers.add(debtSupplierTransaction);
            }

            newRefund.calculator();
            newRefund.calculatorRefundPaymentStatus(request.getTransaction().getAmount());
            existingGRN.calculatorRefundStatus();

            refundInformationRepository.save(newRefund);
            refundDetailRepository.saveAll(refundDetails);
            supplierRepository.save(existingSupplier);
            debtSupplierRepository.saveAll(debtSuppliers);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.REFUND_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getAllByGRN(String grnId) {
        try {
            Optional<GRN> grnOptional = grnRepository.findByIdAndTenantId(grnId, authHelper.getUser().getTenantId());
            if (grnOptional.isEmpty()) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_NOT_FOUND));
            }

            GRN existingGRN = grnOptional.get();
            List<RefundInformation> refundInformations = refundInformationRepository.findAllByGrnAndTenantId(existingGRN, authHelper.getUser().getTenantId());

            List<Map<String, Object>> transactionList = (List<Map<String, Object>>) transactionService
                    .getTransactionByRefundId(existingGRN.getId()).getBody().getData();

            List<GetListByGRNResponse> responses = refundInformations.stream().map(refundInformation -> {
                GetListByGRNResponse response = refundMapper.mapToResponse(refundInformation);
                response.setUserCreatedName(refundInformation.getUserCreated().getFullName());

                Map<String, Object> transaction = transactionList.get(0);
                response.setTransation(transaction);

                List<RefundDetailResponse> refundDetailResponses = refundInformation.getRefundInformationDetails().stream()
                        .map(refundDetail -> {
                            RefundDetailResponse refundDetailResponse = refundMapper.mapToResponse(refundDetail);

                            if (refundDetail.getProduct().getImages() != null && !refundDetail.getProduct().getImages().isEmpty()) {
                                refundDetailResponse.setImage(refundDetail.getProduct().getImages().get(0));
                            }
                            refundDetailResponse.setProductName(refundDetail.getProduct().getName());
                            refundDetailResponse.setProductId(refundDetail.getProduct().getId());
                            refundDetailResponse.setProductSubId(refundDetail.getProduct().getSubId());
                            return refundDetailResponse;
                        }).toList();

                response.setRefundDetails(refundDetailResponses);

                return response;
            }).toList();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.REFUND_GET_ALL_SUCCESSFULLY), responses);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
