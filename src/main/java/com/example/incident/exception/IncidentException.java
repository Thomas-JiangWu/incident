package com.example.incident.exception;

import lombok.Getter;

@Getter
public class IncidentException extends RuntimeException {

    private final int code;

    public IncidentException(int code, String message) {
        super(message);
        this.code = code;
    }
}