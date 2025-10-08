package com.sviat.gamerecommender.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonService {
    private final ObjectMapper objectMapper;

    public JsonService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Loads data from a file using classpath resources.
     * 
     * @param filePath The path to the file (will be normalized for classpath access)
     * @param typeReference The type reference for deserialization
     * @return The deserialized data
     */
    public <T> List<T> loadFromFile(String filePath, TypeReference<List<T>> typeReference) {
        try {
            // Normalize the path for classpath loading
            String cleanPath = filePath;
            if (cleanPath.startsWith("./")) {
                cleanPath = cleanPath.substring(2);
            }
            if (cleanPath.startsWith("src/main/resources/")) {
                cleanPath = cleanPath.substring("src/main/resources/".length());
            }
            
            // Load from classpath
            Resource resource = new ClassPathResource(cleanPath);
            try (InputStream is = resource.getInputStream()) {
                return objectMapper.readValue(is, typeReference);
            }
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
