package hsbc.incident.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hsbc.incident.entity.Incident;
import hsbc.incident.vo.IncidentVO;

public interface IncidentService {
    Page<Incident> list(Integer pageNum, Integer pageSize);

    Incident get(Long id);

    Long save(IncidentVO incidentVO);

    Incident update(Long id, IncidentVO incidentVO);

    void delete(Long id);
}
