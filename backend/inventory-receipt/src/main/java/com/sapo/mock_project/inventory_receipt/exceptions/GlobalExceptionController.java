package com.sapo.mock_project.inventory_receipt.exceptions;

import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionController {
    private final LocalizationUtils localizationUtils;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseObject<Object>> handle(IllegalArgumentException e) {
        return ResponseUtil.error400Response(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseObject<Object>> handle(NoResourceFoundException e) {
        return ResponseUtil.error404Response(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseObject<Object>> handle(ConstraintViolationException e) {
        return ResponseUtil.errorValidationResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject<Object>> handle(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseUtil.error500Response(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseObject<Object>> handle(MissingServletRequestParameterException e) {
        return ResponseUtil.error400Response(e.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseObject<Object>> handle(DataNotFoundException e) {
        return ResponseUtil.error404Response(e.getMessage());
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ResponseObject<Object>> handle(ExpiredTokenException e) {
        return ResponseUtil.error401Response(e.getMessage());
    }

    @ExceptionHandler(NoActionForOperationException.class)
    public ResponseEntity<ResponseObject<Object>> handle(NoActionForOperationException e) {
        return ResponseUtil.error400Response(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseObject<Object>> handleBindException(BindException e) {
        return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}
