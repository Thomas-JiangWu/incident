package com.example.incident.vo;

import com.example.incident.common.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class IncidentVO {
    @NotBlank(groups = ValidGroup.Create.class)
    private String reporter;

    @NotBlank(groups = ValidGroup.Create.class)
    private String title;

    @NotBlank(groups = ValidGroup.Create.class)
    private String description;

    @NotBlank(groups = ValidGroup.Create.class)
    @Pattern(regexp = "Pending|Processing|Resolved")
    private String status;

    @NotBlank(groups = ValidGroup.Create.class)
    @Pattern(regexp = "Low|Medium|High")
    private String priority;
}