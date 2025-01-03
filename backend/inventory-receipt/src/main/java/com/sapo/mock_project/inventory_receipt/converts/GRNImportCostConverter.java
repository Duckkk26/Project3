package com.sapo.mock_project.inventory_receipt.converts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Converter(autoApply = true)
public class GRNImportCostConverter implements AttributeConverter<List<GRNImportCost>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(List<GRNImportCost> attribute) {
        try {

            return objectMapper.writeValueAsString(attribute);

        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert import costs into JSON");
            return null;
        }
    }

    @Override
    public List<GRNImportCost> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<GRNImportCost>>(){}); // Chuyển JSON string thành List
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into list of import costs");
            return null;
        }
    }
}
