package hsbc.incident.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hsbc.incident.entity.Incident;
import hsbc.incident.mapper.IncidentMapper;
import hsbc.incident.service.IncidentService;
import hsbc.incident.vo.IncidentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class IncidentServiceImpl extends ServiceImpl<IncidentMapper, Incident> implements IncidentService {

    public Page<Incident> list(Integer pageNum, Integer pageSize) {
        Page<Incident> page = new Page<>(pageNum, pageSize);
        return this.page(page);
    }

    public Incident get(Long id) {
        return this.getById(id);
    }

    public Long save(IncidentVO incidentVO) {
        Incident incident = new Incident();
        BeanUtils.copyProperties(incidentVO, incident);
        this.save(incident);
        return incident.getId();
    }

    public Incident update(Long id, IncidentVO incidentVO) {
//        if (!incidentRepository.containsKey(id)) {
//            throw new IncidentNotFoundException("Incident not found");
//        }
//        updatedIncident.setId(id);
//        incidentRepository.put(id, updatedIncident);
        return new Incident();
    }

    public void delete(Long id) {
//        if (incidentRepository.remove(id) == null) {
//            throw new IncidentNotFoundException("Incident not found");
//        }
        return;
    }
}
