package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Packing;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.PackingCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.PackingQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.PackingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PackingServiceImpl implements PackingService {
    @Inject
    private PackingCommandAdapter packingCommandAdapter;
    @Inject
    private PackingQueryAdapter packingQueryAdapter;

    @Override
    public void create(Packing dto) {
        packingCommandAdapter.add(List.of(dto));
    }

    @Override
    public void update(Packing dto) {
        packingCommandAdapter.update(List.of(dto));
    }

    @Override
    public void delete(Long id) {
        packingCommandAdapter.delete(List.of(id));
    }

    @Override
    public Packing getById(Long id) {
        return packingQueryAdapter.getById(id);
    }

    @Override
    public List<Packing> getAll(Packing filter) {
        return packingQueryAdapter.getAll(filter);
    }
}
