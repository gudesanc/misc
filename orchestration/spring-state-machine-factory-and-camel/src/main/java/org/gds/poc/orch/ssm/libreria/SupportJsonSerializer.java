package org.gds.poc.orch.ssm.libreria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SupportJsonSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String serialize(T businessCtx) {
        try {
            return objectMapper.writeValueAsString(businessCtx);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
