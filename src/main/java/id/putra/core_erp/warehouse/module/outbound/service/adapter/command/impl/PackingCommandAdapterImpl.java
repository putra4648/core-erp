package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.PackingDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PackingRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.PackingCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PackingCommandAdapterImpl implements PackingCommandAdapter {
    @Inject
    private PackingRepository packingRepository;

    @Override
    public void add(List<PackingDto> dtos) {

    }

    @Override
    public void update(List<PackingDto> dtos) {

    }

    @Override
    public void delete(List<PackingDto> dtos) {
    }
}
