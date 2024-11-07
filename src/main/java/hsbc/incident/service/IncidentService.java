package hsbc.incident.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hsbc.incident.entity.Incident;
import hsbc.incident.vo.IncidentVO;

public interface IncidentService {
    Page<Incident> list(Integer pageNum, Integer pageSize);

    Incident get(Long id);

    Long create(IncidentVO incidentVO);

    Boolean update(Long id, IncidentVO incidentVO);

    Boolean delete(Long id);
}
