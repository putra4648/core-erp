package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.repository.ZoneRepository;
import id.putra.core_erp.service.BaseService;
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

    public void save(Zone entity) {
        zoneRepository.persist(entity);
    }

    public Zone findById(Long id) {
        return zoneRepository.findById(id);
    }

    public List<Zone> findAll(Zone z, Page p) {
        return zoneRepository.findAll().list();
    }

    public void delete(Long id) {
        zoneRepository.deleteById(id);
    }

    @Override
    public void update(Zone entity) {
        zoneRepository.update("code = ?1, name = ?2, type = ?3 where id = ?4", entity.getCode(),
                entity.getName(), entity.getType(), entity.getId());
    }
}
