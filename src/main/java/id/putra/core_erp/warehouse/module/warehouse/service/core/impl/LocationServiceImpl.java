package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Location;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.LocationRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.core.LocationCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LocationServiceImpl implements LocationCoreService {

    @Inject
    private LocationRepository locationRepository;

    @Override
    public Location getLocation(Location dto) {
        return locationRepository.findById(dto.getId());
    }

    @Override
    public void add(List<Location> dtos) {
        locationRepository.persist(dtos);
    }

    @Override
    public void update(List<Location> dtos) {
        dtos.forEach(dto -> locationRepository.getEntityManager().merge(dto));
    }

    @Override
    public void delete(List<Location> dtos) {
        dtos.forEach(dto -> locationRepository.deleteById(dto.getId()));
    }

    @Override
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
