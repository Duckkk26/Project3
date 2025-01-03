package com.sapo.mock_project.inventory_receipt.services.transaction;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.*;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNPaymentStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNReceiveStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.dtos.internal.transaction.AutoCreateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.transaction.GetTotalTransactionResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.transaction.TransactionGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.*;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.exceptions.NoActionForOperationException;
import com.sapo.mock_project.inventory_receipt.mappers.TransactionMapper;
import com.sapo.mock_project.inventory_receipt.repositories.grn.GRNRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.DebtSupplierRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierRepository;
import com.sapo.mock_project.inventory_receipt.repositories.transaction.TransactionCategoryRepository;
import com.sapo.mock_project.inventory_receipt.repositories.transaction.TransactionRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.GetTotalTransactionSpecification;
import com.sapo.mock_project.inventory_receipt.services.specification.TransactionSpecification;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.DateUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Dịch vụ xử lý các thao tác liên quan đến  (transaction).
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final SupplierRepository supplierRepository;
    private final GRNRepository grnRepository;

    private final TransactionMapper transactionMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;
    private final DebtSupplierRepository debtSupplierRepository;

    /**
     * Tạo mới một phiếu thu/chi dựa trên yêu cầu đầu vào.
     * Nếu mã phiếu thu/chi đã tồn tại, trả về thông báo lỗi.
     *
     * @param request Đối tượng chứa thông tin tạo phiếu thu/chi.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createTransaction(CreateTransactionRequest request) {
        try {
            if (request.getSubId() != null && !request.getSubId().toString().isEmpty()
                && transactionRepository.existsBySubIdAndTenantId(request.getSubId(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.TRANSACTION_ID_EXISTED));
            }
            TransactionCategory transactionCategory = transactionCategoryRepository.findByIdAndTenantId(request.getTransactionCategoryId(), authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_CATEGORY_NOT_FOUND)));
            if (transactionCategory.getId().equals("TSC00001") || transactionCategory.getId().equals("TSC00002")) {
                throw new NoActionForOperationException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_CANNOT_CREATE_AUTO));
            }
            if (transactionCategory.getType() != request.getType()) {
                throw new NoActionForOperationException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_TYPE_NOT_MATCH));
            }

            User userCreated = authHelper.getUser();

            Transaction newTransaction = transactionMapper.mapToEntity(request);
            newTransaction.setStatus(TransactionStatus.COMPLETED);
            newTransaction.setUserCreated(userCreated);
            newTransaction.setCategory(transactionCategory);

            if (request.isAutoDebt()) {
                if (request.getRecipientGroup().equals(PrefixId.SUPPLIER)) {
                    Optional<Supplier> supplierOptional = supplierRepository.findByIdAndTenantId(request.getRecipientId(), authHelper.getUser().getTenantId());
                    if (supplierOptional.isEmpty()) {
                        return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND));
                    }

                    Supplier supplier = supplierOptional.get();
                    if (request.getType().equals(TransactionType.INCOME)) {
                        supplier.setCurrentDebt(supplier.getCurrentDebt().add(request.getAmount()));
                    } else {
                        supplier.setCurrentDebt(supplier.getCurrentDebt().subtract(request.getAmount()));
                    }

                    supplierRepository.save(supplier);
                }
            }

            transactionRepository.save(newTransaction);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Tự động tạo phiếu thu/chi dựa trên yêu cầu đầu vào mà không kiểm tra mã phiếu thu/chi.
     *
     * @param request Đối tượng chứa thông tin tạo phiếu thu/chi tự động.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> autoCreateTransaction(AutoCreateTransactionRequest request) {
        try {
            User userCreated = authHelper.getUser();
            Optional<TransactionCategory> transactionCategory = transactionCategoryRepository
                    .findById(request.getType() == TransactionType.INCOME ? "TSC00001" : "TSC00002");
            if (transactionCategory.isEmpty()) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_CATEGORY_NOT_FOUND));
            }

            Transaction newTransaction = transactionMapper.mapToEntity(request);
            newTransaction.setUserCreated(userCreated);
            newTransaction.setStatus(TransactionStatus.COMPLETED);
            newTransaction.setCategory(transactionCategory.get());

            transactionRepository.save(newTransaction);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CREATE_SUCCESSFULLY), newTransaction.getId());
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lọc danh sách phiếu thu/chi theo tiêu chí, phân trang và sắp xếp.
     * Các trường dữ liệu sẽ được ánh xạ động dựa trên `filterParams`.
     *
     * @param request      Đối tượng chứa các tiêu chí lọc.
     * @param filterParams Map chứa các trường cần bao gồm hoặc loại bỏ.
     * @param sort         Hướng sắp xếp (ASC hoặc DESC).
     * @param sortField    Trường được sắp xếp.
     * @param page         Số trang hiện tại.
     * @param size         Số lượng mục trên một trang.
     * @return ResponseEntity chứa danh sách phiếu thu/chi đã lọc và thông tin phân trang.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> filterTransaction(GetListTransactionRequest request,
                                                                    Map<String, Boolean> filterParams,
                                                                    String sort, String sortField,
                                                                    int page, int size) {
        try {
            TransactionSpecification specification = new TransactionSpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(sort), sortField));

            Page<Transaction> transactionPage = transactionRepository.findAll(specification, pageable);

            List<TransactionGetListResponse> responseList = transactionPage.stream().map(transaction -> {
                TransactionGetListResponse response = new TransactionGetListResponse();

                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
                    String field = entry.getKey();
                    Boolean includeField = entry.getValue();

                    // Ánh xạ các trường dựa trên filterParams
                    if (includeField) {
                        if (field.equals("user_created_name")) {
                            response.setUserCreatedName(transaction.getUserCreated().getFullName());
                        } else if (field.equals("transaction_category_name")) {
                            response.setTransactionCategoryName(transaction.getCategory().getName());
                        } else {
                            try {
                                // Lấy giá trị của trường từ đối tượng supplier
                                Object fieldValue = CommonUtils.getFieldValue(transaction, field);

                                // Nếu giá trị không null, tiếp tục xử lý
                                if (fieldValue != null) {
                                    // Kiểm tra kiểu dữ liệu của trường cần set
                                    Field responseField = CommonUtils.getFieldFromClassHierarchy(response.getClass(), StringUtils.snakeCaseToEqualCamelCase(field));
                                    responseField.setAccessible(true); // Cho phép truy cập vào trường private

                                    // Kiểm tra và chuyển đổi kiểu dữ liệu nếu cần
                                    Object convertedValue = CommonUtils.convertValueForField(responseField, fieldValue);

                                    // Đặt giá trị cho trường trong đối tượng response
                                    responseField.set(response, convertedValue);
                                }
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                // Xử lý lỗi nếu không thể lấy hoặc set giá trị cho trường
                                throw new RuntimeException("Failed to map field " + field + " using reflection", e);
                            }
                        }
                    }
                }

                return response;
            }).toList();

            Pagination pagination = Pagination.<Object>builder()
                    .data(responseList)
                    .totalPage(transactionPage.getTotalPages())
                    .totalItems(transactionPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin phiếu thu/chi dựa trên mã phiếu thu/chi và yêu cầu đầu vào.
     * Nếu mã phiếu thu/chi không tồn tại, trả về lỗi.
     *
     * @param id      Mã phiếu thu/chi cần cập nhật.
     * @param request Đối tượng chứa thông tin cập nhật phiếu thu/chi.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateTransaction(String id, UpdateTransactionRequest request) {
        try {
            Transaction transaction = transactionRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_NOT_FOUND)));

            if (request.getSubId() != null && !request.getSubId().equals(transaction.getId())
                && transactionRepository.existsBySubIdAndTenantId(request.getSubId(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.TRANSACTION_ID_EXISTED));
            }

            transactionMapper.updateFromToDTO(request, transaction);

            transactionRepository.save(transaction);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Hủy bỏ phiếu thu/chi dựa trên mã phiếu thu/chi. Nếu phiếu thu/chi không thể hủy, ném ngoại lệ NoActionForOperationException.
     *
     * @param id Mã phiếu thu/chi cần hủy.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> cancelTransaction(String id) {
        try {
            Transaction transaction = transactionRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_NOT_FOUND)));
            if (transaction.getCategory().getId().equals("TSC00001") || transaction.getCategory().getId().equals("TSC00002")) {
                throw new NoActionForOperationException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_NOT_CANCELABLE));
            }

            transaction.setStatus(TransactionStatus.CANCELLED);

            transactionRepository.save(transaction);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CANCEL_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getTransactionByRefundId(String grnId) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.YYYYMMDDHHMMSS);

            List<Transaction> transactions = transactionRepository.findByRefundIdAndTenantId(grnId, authHelper.getUser().getTenantId());

            List<Map<String, Object>> responses = transactions.stream().map(refund -> {
                Map<String, Object> response = new HashMap<>();
                response.put("id", refund.getId());
                response.put("amount", refund.getAmount());
                String createdAtFormatted = refund.getCreatedAt().format(formatter);
                String updatedAtFormatted = refund.getUpdatedAt().format(formatter);

                response.put("created_at", createdAtFormatted);
                response.put("updated_at", updatedAtFormatted);

                response.put("user_created_name", refund.getUserCreated().getFullName());
                response.put("payment_method", refund.getPaymentMethod());

                return response;
            }).toList();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_GET_ALL_SUCCESSFULLY), responses);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getTotalTransaction(GetTotalRequest request, int page, int size) {
        try {
            GetTotalTransactionSpecification specification = new GetTotalTransactionSpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<Transaction> transactionPage = transactionRepository.findAll(specification, pageable);
            List<GetTotalTransactionResponse> responseList = transactionPage.getContent().stream().map(transaction -> {
                GetTotalTransactionResponse response = transactionMapper.mapToTotalResponse(transaction);
                response.setCategoryName(transaction.getCategory().getName());

                return response;
            }).toList();

            Pagination pagination = Pagination.<Object>builder()
                    .data(responseList)
                    .totalPage(transactionPage.getTotalPages())
                    .totalItems(transactionPage.getTotalElements())
                    .build();

            List<Object[]> totalValues = transactionRepository.getTotalValue(request.getMode(), request.getDateType(),
                    DateUtils.getDateTimeFrom(request.getDateFrom()),
                    DateUtils.getDateTimeFrom(request.getDateTo()),
                    authHelper.getUser().getTenantId());

            Map<String, Object> response = new HashMap<>();
            response.put("total_income", totalValues.get(0)[0]);
            response.put("total_expense", totalValues.get(0)[1]);
            response.put("total_before", totalValues.get(0)[2]);
            response.put("pagination", pagination);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_GET_ALL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> paymentGRN(CreateTransactionGRNRequest request) {
        try {
            Optional<GRN> grn = grnRepository.findByIdAndTenantId(request.getGrnId(), authHelper.getUser().getTenantId());
            if (grn.isEmpty()) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_NOT_FOUND));
            }
            if (grn.get().getTotalPaid().add(request.getAmount()).compareTo(grn.get().getTotalValue()) > 0) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_GRN_AMOUNT_INVALID));
            }
            TransactionCategory transactionCategory = transactionCategoryRepository.findById("TSC00002")
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_CATEGORY_NOT_FOUND)));

            GRN exsitingGrn = grn.get();
            exsitingGrn.setTotalPaid(exsitingGrn.getTotalPaid().add(request.getAmount()));
            if (exsitingGrn.getTotalPaid().compareTo(exsitingGrn.getTotalValue()) < 0) {
                exsitingGrn.setPaymentStatus(GRNPaymentStatus.PARTIAL_PAID);
            } else if (exsitingGrn.getTotalPaid().compareTo(exsitingGrn.getTotalValue()) == 0) {
                exsitingGrn.setPaymentStatus(GRNPaymentStatus.PAID);
            } else {
                exsitingGrn.setPaymentStatus(GRNPaymentStatus.UNPAID);
            }

            Supplier supplier = exsitingGrn.getSupplier();
            supplier.setCurrentDebt(supplier.getCurrentDebt().subtract(request.getAmount()));

            DebtSupplier debtSupplier = DebtSupplier.builder()
                    .amount(request.getAmount())
                    .debtAfter(supplier.getCurrentDebt())
                    .referenceCode(PrefixId.GRN)
                    .referenceId(exsitingGrn.getId())
                    .note("Thanh toán phiếu nhập " + exsitingGrn.getId())
                    .userCreated(authHelper.getUser())
                    .supplier(supplier)
                    .build();

            Transaction newTransaction = Transaction.builder()
                    .amount(request.getAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .referenceCode(PrefixId.GRN)
                    .referenceId(exsitingGrn.getId())
                    .recipientGroup(PrefixId.SUPPLIER)
                    .recipientId(exsitingGrn.getSupplier().getId())
                    .recipientName(exsitingGrn.getSupplier().getName())
                    .type(TransactionType.EXPENSE)
                    .status(TransactionStatus.COMPLETED)
                    .category(transactionCategory)
                    .note("Thanh toán phiếu nhập " + grn.get().getId())
                    .userCreated(authHelper.getUser())
                    .build();

            GRNPaymentMethod paymentMethod = GRNPaymentMethod.builder()
                    .amount(request.getAmount())
                    .method(request.getPaymentMethod().name())
                    .date(LocalDateTime.now())
                    .reference(newTransaction.getId())
                    .build();
            List<GRNPaymentMethod> paymentMethods = exsitingGrn.getPaymentMethods();
            if (paymentMethods == null || paymentMethods.isEmpty()) {
                paymentMethods = List.of(paymentMethod);
            } else {
                paymentMethods.add(paymentMethod);
            }
            exsitingGrn.setPaymentMethods(paymentMethods);
            if (exsitingGrn.getTotalPaid().compareTo(exsitingGrn.getTotalValue()) == 0 && exsitingGrn.getReceivedStatus() == GRNReceiveStatus.ENTERED) {
                exsitingGrn.setStatus(GRNStatus.COMPLETED);
            }

            transactionRepository.save(newTransaction);
            debtSupplierRepository.save(debtSupplier);
            supplierRepository.save(supplier);
            grnRepository.save(exsitingGrn);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}