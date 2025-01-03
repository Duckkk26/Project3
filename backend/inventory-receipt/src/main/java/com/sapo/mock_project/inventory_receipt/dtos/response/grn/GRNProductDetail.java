package com.sapo.mock_project.inventory_receipt.dtos.response.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO chứa thông tin chi tiết về sản phẩm trong phiếu nhập kho (GRN).
 */
@Data
public class GRNProductDetail {

    /**
     * ID của sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "ID của sản phẩm trong phiếu nhập kho", example = "PROD12345")
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("types")
    private List<ProductType> types;

    /**
     * Đơn vị của sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "Đơn vị của sản phẩm trong phiếu nhập kho", example = "Cái")
    @JsonProperty("unit")
    private String unit;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_sub_id")
    private String productSubId;

    /**
     * Số lượng của sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "Số lượng của sản phẩm trong phiếu nhập kho", example = "50")
    @JsonProperty("quantity")
    private BigDecimal quantity;

    /**
     * Chiết khấu áp dụng cho sản phẩm.
     */
    @Schema(description = "Chiết khấu áp dụng cho sản phẩm", example = "500.00")
    @JsonProperty("discount")
    private BigDecimal discount;

    /**
     * Số tiền thuế áp dụng cho sản phẩm.
     */
    @Schema(description = "Số tiền thuế áp dụng cho sản phẩm", example = "1000.00")
    @JsonProperty("tax")
    private BigDecimal tax;

    /**
     * Giá của sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "Giá của sản phẩm trong phiếu nhập kho", example = "10000.00")
    @JsonProperty("price")
    private BigDecimal price;

}
