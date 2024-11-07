package hsbc.incident.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hsbc.incident.common.Constants;
import hsbc.incident.entity.Incident;
import hsbc.incident.exception.IncidentException;
import hsbc.incident.mapper.IncidentMapper;
import hsbc.incident.service.IncidentService;
import hsbc.incident.vo.IncidentVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class IncidentServiceImpl extends ServiceImpl<IncidentMapper, Incident> implements IncidentService {

    @Cacheable(value = "incidentListCache")
    public Page<Incident> list(Integer pageNum, Integer pageSize) {
        Page<Incident> page = new Page<>(pageNum, pageSize);
        return this.page(page);
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
    public Long save(IncidentVO incidentVO) {
        Incident incident = new Incident();
        incident.setDescription(incidentVO.getDescription());
        incident.setStatus(incidentVO.getStatus());
        this.save(incident);
        return incident.getId();
    }


    @CachePut(value = "incidentCache", key = "#id")
    @CacheEvict(value = "incidentListCache", allEntries = true)
    public Boolean update(Long id, IncidentVO incidentVO) {
        Incident incident = this.get(id);
        incident.setDescription(incidentVO.getDescription());
        incident.setStatus(incidentVO.getStatus());
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
