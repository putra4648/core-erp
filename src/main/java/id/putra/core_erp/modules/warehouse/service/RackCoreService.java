package id.putra.core_erp.modules.warehouse.service;

import id.putra.core_erp.service.BaseService;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.repository.RackRepository;
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

    public Rack getRack(Rack rackDto) {
        return rackRepository.findById(rackDto.getId());
    }

    public void save(Rack entity) {
        rackRepository.persist(entity);
    }

    public Rack findById(Long id) {
        return rackRepository.findById(id);
    }

    public Iterable<Rack> findAll(Rack r, Page p) {
        return rackRepository.findAll().list();
    }

    public void add(List<Rack> dtos) {
        rackRepository.persist(dtos);
    }

    public void update(List<Rack> dtos) {
        dtos.forEach(dto -> rackRepository.getEntityManager().merge(dto));
    }

    public void update(Rack entity) {
        rackRepository.getEntityManager().merge(entity);
    }

    public void delete(List<Rack> dtos) {
        dtos.forEach(dto -> rackRepository.deleteById(dto.getId()));
    }

    public void delete(Long id) {
        rackRepository.deleteById(id);
    }

    public DataProvider<Rack, Void> getRacks(Rack param, Page page) {
        String pattern = "%" + (param.getName() == null ? "" : param.getName().trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> rackRepository.find("lower(name) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(rackRepository.count("lower(name) like ?1", pattern)));
    }

}
