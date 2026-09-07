package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.RackDto;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.RackRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.RackCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RackCommandAdapterImpl implements RackCommandAdapter {

    @Inject
    private RackRepository rackRepository;

    @Override
    public void add(List<RackDto> dtos) {
    }

    @Override
    public void update(List<RackDto> dtos) {
    }

    @Override
    public void delete(List<RackDto> dtos) {
    }

}
