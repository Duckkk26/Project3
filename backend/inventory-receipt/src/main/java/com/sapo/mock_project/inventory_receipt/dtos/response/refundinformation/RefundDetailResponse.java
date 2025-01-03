package com.sapo.mock_project.inventory_receipt.dtos.response.refundinformation;

import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundDetailResponse {
    private String id;

    private String subId;

    private BigDecimal quantity;

    private BigDecimal refundedPrice;

    private BigDecimal amount;

    private ProductImage image;

    private String productName;

    private String productId;

    private String productSubId;
}
