package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import id.putra.core_erp.modules.warehouse.model.Location;
import id.putra.core_erp.modules.warehouse.repository.LocationRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LocationCoreService implements BaseService<Location> {
    private final LocationRepository locationRepository;

    public LocationCoreService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void save(Location entity) {
        locationRepository.persist(entity);
    }

    public Location findById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> findAll(Location l, Page p) {
        return locationRepository.findAll().list();
    }

    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void update(Location entity) {
        locationRepository.update("name = ?1, rackId = ?2, binNumber = ?3, type = ?4, where id = ?5",
                entity.getName(), entity.getRack().getId(), entity.getBinNumber(), entity.getType(),
                entity.getId());
    }
}
