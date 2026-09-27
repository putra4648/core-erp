package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.repository.RackRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class RackCoreService implements BaseService<Rack> {
    private final RackRepository rackRepository;

    public RackCoreService(RackRepository rackRepository) {
        this.rackRepository = rackRepository;
    }

    public void save(Rack entity) {
        rackRepository.persist(entity);
    }

    public Rack findById(Long id) {
        return rackRepository.findById(id);
    }

    public List<Rack> findAll(Rack r, Page p) {
        return rackRepository.findAll().list();
    }

    public void delete(Long id) {
        rackRepository.deleteById(id);
    }

    @Override
    public void update(Rack entity) {
        rackRepository.update("name = ?1, zoneId = ?2, rows = ?3, cols = ?4 where id = ?5",
                entity.getName(), entity.getZone().getId(), entity.getRows(), entity.getCols(), entity.getId());
    }
}
