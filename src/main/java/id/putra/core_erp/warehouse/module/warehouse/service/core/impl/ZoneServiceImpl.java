package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Zone;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.ZoneRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.core.ZoneCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ZoneServiceImpl implements ZoneCoreService {

    @Inject
    private ZoneRepository zoneRepository;

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
