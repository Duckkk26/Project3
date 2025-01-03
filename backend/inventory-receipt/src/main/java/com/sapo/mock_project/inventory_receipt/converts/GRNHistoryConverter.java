package com.sapo.mock_project.inventory_receipt.converts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNHistory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Converter(autoApply = true)
public class GRNHistoryConverter implements AttributeConverter<List<GRNHistory>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(List<GRNHistory> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute); // Chuyển List thành JSON string
        } catch (JsonProcessingException jpe) {
            log.error("Cannot convert grn History into JSON. Error: {}", jpe.getMessage(), jpe);
            return null;
        }
    }

    @Override
    public List<GRNHistory> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<GRNHistory>>(){}); // Chuyển JSON string thành List
        } catch (JsonProcessingException e) {
            log.error("Cannot convert JSON into list of grn History. Error: {}", e.getMessage(), e);
            return null;
        }
    }
}
