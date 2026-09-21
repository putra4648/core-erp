package id.putra.core_erp.modules.warehouse.service.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.repository.RackRepository;
import id.putra.core_erp.modules.warehouse.service.RackCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class RackServiceImpl implements RackCoreService {

    private final RackRepository rackRepository;

    public RackServiceImpl(RackRepository rackRepository) {
        this.rackRepository = rackRepository;
    }

    @Override
    public Rack getRack(Rack rackDto) {
        return rackRepository.findById(rackDto.getId());
    }

    @Override
    public void add(List<Rack> dtos) {
        rackRepository.persist(dtos);
    }

    @Override
    public void update(List<Rack> dtos) {
        dtos.forEach(dto -> rackRepository.getEntityManager().merge(dto));
    }

    @Override
    public void delete(List<Rack> dtos) {
        dtos.forEach(dto -> rackRepository.deleteById(dto.getId()));
    }

    @Override
    public DataProvider<Rack, Void> getRacks(Rack param, Page page) {
        String pattern = "%" + (param.getName() == null ? "" : param.getName().trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> rackRepository.find("lower(name) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(rackRepository.count("lower(name) like ?1", pattern)));
    }

}
