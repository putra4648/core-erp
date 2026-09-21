package id.putra.core_erp.modules.warehouse.service.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.repository.ZoneRepository;
import id.putra.core_erp.modules.warehouse.service.ZoneCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ZoneServiceImpl implements ZoneCoreService {

    private final ZoneRepository zoneRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone getZone(Zone zoneDto) {
        return zoneRepository.findById(zoneDto.getId());
    }

    @Override
    public void add(List<Zone> dtos) {
        zoneRepository.persist(dtos);
    }

    @Override
    public void update(List<Zone> dtos) {
        dtos.forEach(dto -> zoneRepository.getEntityManager().merge(dto));
    }

    @Override
    public void delete(List<Zone> dtos) {
        dtos.forEach(dto -> zoneRepository.deleteById(dto.getId()));
    }

    @Override
    public DataProvider<Zone, Void> getZones(Zone param, Page page) {
        String pattern = "%" + (param.getName() == null ? "" : param.getName().trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> zoneRepository.find("lower(name) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(zoneRepository.count("lower(name) like ?1", pattern)));
    }

}
