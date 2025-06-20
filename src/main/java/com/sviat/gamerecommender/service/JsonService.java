package com.sviat.gamerecommender.service;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonService {
    private final ObjectMapper objectMapper;

    public JsonService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> List<T> loadFromFile(String filePath, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(new File(filePath), typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error loading data from file: " + filePath, e);
        }
    }

    public <T> void saveToFile(String filePath, List<T> data) {
        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (Exception e) {
            throw new RuntimeException("Error saving data to file: " + filePath, e);
        }
    }
}
