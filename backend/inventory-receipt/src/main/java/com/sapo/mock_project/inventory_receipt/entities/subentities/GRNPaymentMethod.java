package com.sapo.mock_project.inventory_receipt.entities.subentities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GRNPaymentMethod {
    private String method;

    private BigDecimal amount;

    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;

    private String reference;
}
