package com.example.incident.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.incident.entity.Incident;
import com.example.incident.vo.IncidentVO;

public interface IncidentService {
    Page<Incident> list(String status, String priority, Integer pageNum, Integer pageSize);

    Incident get(Long id);

    Long create(IncidentVO incidentVO);

    Boolean update(Long id, IncidentVO incidentVO);

    Boolean delete(Long id);
}
