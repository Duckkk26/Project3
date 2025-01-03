package com.sapo.mock_project.inventory_receipt.entities.subentities;

import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.validator.ValidNumber;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GRNImportCost {
    private String name;

    private BigDecimal value;
}
