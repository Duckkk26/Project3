package com.sapo.mock_project.inventory_receipt.converts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Converter(autoApply = true)
public class ProductTypeConverter implements AttributeConverter<List<ProductType>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(List<ProductType> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert ProductType into JSON");
            return null;
        }
    }

    @Override
    public List<ProductType> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<ProductType>>() {});
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into ProductType");
            return null;
        }
    }
}
