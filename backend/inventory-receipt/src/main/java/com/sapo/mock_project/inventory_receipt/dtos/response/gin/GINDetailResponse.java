package com.sapo.mock_project.inventory_receipt.dtos.response.gin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.GINProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GINDetailResponse extends BaseResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("status")
    private GINStatus status;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    @JsonProperty("user_balanced_name")
    private String userBalancedName;

    @JsonProperty("user_inspection_name")
    private String userInspectionName;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note")
    private String note;

    @JsonProperty("balanced_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime balancedAt;

    @JsonProperty("products")
    private List<GINProductDetailResponse> products;
}
