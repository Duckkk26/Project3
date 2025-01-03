package com.sapo.mock_project.inventory_receipt.dtos.response.gin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExportDataResponse extends BaseResponse {
    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("user_inspection_name")
    private String userInspectionName;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    @JsonProperty("user_balanced_name")
    private String userBalancedName;

    @JsonProperty("balanced_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime balancedAt;

    @JsonProperty("status")
    private GINStatus status;

    @JsonProperty("products")
    private List<ProductGINExportResponse> products;

    @Data
    public static class ProductGINExportResponse {
        @JsonProperty("id")
        private String id;

        @JsonProperty("product_sub_id")
        private String productSubId;

        @JsonProperty("product_name")
        private String productName;

        @JsonProperty("unit")
        private String unit;

        @JsonProperty("real_quantity")
        private BigDecimal realQuantity;

        @JsonProperty("actual_stock")
        private BigDecimal actualStock;

        @JsonProperty("discrepancy_quantity")
        private BigDecimal discrepancyQuantity;

        @JsonProperty("reason")
        private String reason;

        @JsonProperty("note")
        private String note;
    }
}
