package com.sapo.mock_project.inventory_receipt.entities.subentities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GRNHistory {
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;

    private String userExecuted;

    private String function;

    private String operation;
}
