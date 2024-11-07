package com.example.incident.entity;

import lombok.Data;

@Data
public class Incident {
    private Long id;
    private String description;
    private String status;
}