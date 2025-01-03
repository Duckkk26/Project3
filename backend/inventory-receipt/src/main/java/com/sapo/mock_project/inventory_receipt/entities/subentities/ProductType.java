package com.sapo.mock_project.inventory_receipt.entities.subentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {
    private String name;

    private String value;
}
