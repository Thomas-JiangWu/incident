package com.example.incident.vo;

import com.example.incident.common.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IncidentVO {
    @NotBlank(groups = ValidGroup.Create.class)
    private String description;

    @NotBlank(groups = ValidGroup.Create.class)
    private String status;
}