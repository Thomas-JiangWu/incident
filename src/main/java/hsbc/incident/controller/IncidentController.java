package hsbc.incident.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hsbc.incident.common.Constants;
import hsbc.incident.common.Response;
import hsbc.incident.common.ValidGroup;
import hsbc.incident.entity.Incident;
import hsbc.incident.service.IncidentService;
import hsbc.incident.vo.IncidentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/incidents")
@Validated
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public Response<Page<Incident>> list(@RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE_NUM) @Min(value = 1) Integer pageNum,
                                         @RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer pageSize) {
        return Response.success(incidentService.list(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Response<Incident> get(@PathVariable Long id) {
        return Response.success(incidentService.get(id));
    }

    @PostMapping
    public Response<Long> create(@Validated(value = ValidGroup.Create.class) @RequestBody IncidentVO incidentVO) {
        return Response.success(incidentService.create(incidentVO));
    }

    @PutMapping("/{id}")
    public Response<Boolean> update(@PathVariable Long id, @RequestBody IncidentVO incidentVO) {
        return Response.success(incidentService.update(id, incidentVO));
    }

    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable Long id) {
        return Response.success(incidentService.delete(id));
    }
}
