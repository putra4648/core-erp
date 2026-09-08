package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Packing;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PackingRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.PackingQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PackingQueryAdapterImpl implements PackingQueryAdapter {
    @Inject
    private PackingRepository packingRepository;

    @Override
    public Packing getById(Long id) {
        return packingRepository.findById(id);
    }

    @Override
    public List<Packing> getAll(Packing filter) {
        return packingRepository.findAll().list();
    }
}
