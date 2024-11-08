package com.example.incident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.incident.common.Constants;
import com.example.incident.entity.Incident;
import com.example.incident.exception.IncidentException;
import com.example.incident.mapper.IncidentMapper;
import com.example.incident.service.IncidentService;
import com.example.incident.vo.IncidentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IncidentServiceImpl extends ServiceImpl<IncidentMapper, Incident> implements IncidentService {

    @Cacheable(value = "incidentListCache")
    public Page<Incident> list(String status, String priority, Integer pageNum, Integer pageSize) {
        QueryWrapper<Incident> query = new QueryWrapper<>();
        query.eq(StringUtils.isNotBlank(status), "status", status);
        query.eq(StringUtils.isNotBlank(priority), "priority", priority);
        query.orderByDesc("created_time");
        Page<Incident> page = new Page<>(pageNum, pageSize);
        return this.page(page, query);
    }

    @Cacheable(value = "incidentCache", key = "#id")
    public Incident get(Long id) {
        Incident incident = this.getById(id);
        if (incident == null) {
            throw new IncidentException(Constants.RESPONSE_CODE_INCIDENT_NOT_FOUND, String.format("incident id %s not found", id));
        }
        return incident;
    }

    @CacheEvict(value = "incidentListCache", allEntries = true)
    public Long create(IncidentVO incidentVO) {
        Incident incident = new Incident();
        BeanUtils.copyProperties(incidentVO, incident);
        this.save(incident);
        return incident.getId();
    }


    @Caching(evict = {
            @CacheEvict(value = "incidentCache", key = "#id"),
            @CacheEvict(value = "incidentListCache", allEntries = true)
    })
    public Boolean update(Long id, IncidentVO incidentVO) {
        Incident incident = this.get(id);
        BeanUtils.copyProperties(incidentVO, incident);
        incident.setModifiedTime(new Date());
        return this.updateById(incident);
    }

    @Caching(evict = {
            @CacheEvict(value = "incidentCache", key = "#id"),
            @CacheEvict(value = "incidentListCache", allEntries = true)
    })
    public Boolean delete(Long id) {
        Incident incident = this.get(id);
        return this.removeById(id);
    }
}
