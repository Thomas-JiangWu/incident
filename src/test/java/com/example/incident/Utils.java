package com.example.incident;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    static public String toJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
