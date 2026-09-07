package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.RackDto;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.RackCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.RackQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.RackCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RackServiceImpl implements RackCoreService {

    @Inject
    private RackQueryAdapter rackQueryAdapter;

    @Inject
    private RackCommandAdapter rackCommandAdapter;

    @Override
    public RackDto getRack(RackDto rackDto) {
        return rackQueryAdapter.getRackById(rackDto);
    }

    @Override
    public void add(List<RackDto> dtos) {
        rackCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<RackDto> dtos) {
        rackCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<RackDto> dtos) {
        rackCommandAdapter.delete(dtos);
    }

}
