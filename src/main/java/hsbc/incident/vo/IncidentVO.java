package hsbc.incident.vo;

import hsbc.incident.common.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IncidentVO {
    private Long id;

    @NotBlank(groups = ValidGroup.Create.class)
    private String description;

    @NotBlank(groups = ValidGroup.Create.class)
    private String status;
}