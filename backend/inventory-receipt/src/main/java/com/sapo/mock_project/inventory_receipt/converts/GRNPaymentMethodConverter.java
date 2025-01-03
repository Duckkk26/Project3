package com.sapo.mock_project.inventory_receipt.converts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Converter(autoApply = true)
public class GRNPaymentMethodConverter implements AttributeConverter<List<GRNPaymentMethod>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(List<GRNPaymentMethod> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            log.error("Cannot convert payment methods into JSON. Error: {}", jpe.getMessage(), jpe);
            return null;
        }
    }


    @Override
    public List<GRNPaymentMethod> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<GRNPaymentMethod>>(){}); // Chuyển JSON string thành danh sách
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into list of payment methods");
            return null;
        }
    }
}
