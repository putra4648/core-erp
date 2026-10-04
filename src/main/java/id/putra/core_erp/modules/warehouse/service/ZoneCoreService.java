package id.putra.core_erp.modules.warehouse.service;

import id.putra.core_erp.service.BaseService;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.repository.ZoneRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ZoneCoreService implements BaseService<Zone> {

    private final ZoneRepository zoneRepository;

    public ZoneCoreService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Zone getZone(Zone zoneDto) {
        return zoneRepository.findById(zoneDto.id);
    }

    public void save(Zone entity) {
        zoneRepository.persist(entity);
    }

    public Zone findById(Long id) {
        return zoneRepository.findById(id);
    }

    public Iterable<Zone> findAll(Zone z, Page p) {
        return zoneRepository.findAll().list();
    }

    public void add(List<Zone> dtos) {
        zoneRepository.persist(dtos);
    }

    public void update(List<Zone> dtos) {
        dtos.forEach(dto -> zoneRepository.getEntityManager().merge(dto));
    }

    public void update(Zone entity) {
        zoneRepository.getEntityManager().merge(entity);
    }

    public void delete(List<Zone> dtos) {
        dtos.forEach(dto -> zoneRepository.deleteById(dto.id));
    }

    public void delete(Long id) {
        zoneRepository.deleteById(id);
    }

    public DataProvider<Zone, Void> getZones(Zone param, Page page) {
        String pattern = "%" + (param.getName() == null ? "" : param.getName().trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> zoneRepository.find("lower(name) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(zoneRepository.count("lower(name) like ?1", pattern)));
    }

}
