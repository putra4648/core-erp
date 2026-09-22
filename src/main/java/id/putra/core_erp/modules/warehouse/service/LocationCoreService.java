package id.putra.core_erp.modules.warehouse.service;

import id.putra.core_erp.service.BaseService;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Location;
import id.putra.core_erp.modules.warehouse.repository.LocationRepository;
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

    public Location getLocation(Location dto) {
        return locationRepository.findById(dto.getId());
    }

    public void save(Location entity) {
        locationRepository.persist(entity);
    }

    public Location findById(Long id) {
        return locationRepository.findById(id);
    }

    public Iterable<Location> findAll(Location l, Page p) {
        return locationRepository.findAll().list();
    }

    public void add(List<Location> dtos) {
        locationRepository.persist(dtos);
    }

    public void update(List<Location> dtos) {
        dtos.forEach(dto -> locationRepository.getEntityManager().merge(dto));
    }

    public void update(Location entity) {
        locationRepository.getEntityManager().merge(entity);
    }

    public void delete(List<Location> dtos) {
        dtos.forEach(dto -> locationRepository.deleteById(dto.getId()));
    }

    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    public DataProvider<Location, Void> getLocations(Location param, Page page) {
        String pattern = "%" + (param.getName() == null ? "" : param.getName().trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> locationRepository.find("lower(name) like ?1 or lower(binNumber) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(locationRepository.count(
                        "lower(name) like ?1 or lower(binNumber) like ?1", pattern)));
    }

}
