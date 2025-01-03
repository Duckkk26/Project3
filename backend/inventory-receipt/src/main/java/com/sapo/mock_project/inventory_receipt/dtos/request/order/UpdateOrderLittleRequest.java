package com.sapo.mock_project.inventory_receipt.dtos.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateOrderLittleRequest {
    @JsonProperty("note")
    private String note;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note_details")
    private List<Map<String, String>> noteDetails;
}
