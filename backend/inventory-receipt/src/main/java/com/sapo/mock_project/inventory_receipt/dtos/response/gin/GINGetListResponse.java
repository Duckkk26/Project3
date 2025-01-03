package com.sapo.mock_project.inventory_receipt.dtos.response.gin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GINGetListResponse extends BaseResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("status")
    private GINStatus status;

    @JsonProperty("balanced_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime balancedAt;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    @JsonProperty("user_balanced_name")
    private String userBalancedName;

    @JsonProperty("user_inspection_name")
    private String userInspectionName;

    @JsonProperty("note")
    private String note;
}
