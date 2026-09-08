package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Rack;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.RackCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.RackQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.RackCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RackServiceImpl implements RackCoreService {

    @Inject
    private RackQueryAdapter rackQueryAdapter;

    @Inject
    private RackCommandAdapter rackCommandAdapter;

    @Override
    public Rack getRack(Rack rackDto) {
        return rackQueryAdapter.getRackById(rackDto);
    }

    @Override
    public void add(List<Rack> dtos) {
        rackCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<Rack> dtos) {
        rackCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<Rack> dtos) {
        rackCommandAdapter.delete(dtos);
    }

    @Override
    public DataProvider<Rack, Void> getRacks(Rack param, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRacks'");
    }

}
