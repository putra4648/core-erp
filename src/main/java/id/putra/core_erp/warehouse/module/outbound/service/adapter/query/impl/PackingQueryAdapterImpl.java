package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.PackingDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PackingRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.PackingQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PackingQueryAdapterImpl implements PackingQueryAdapter {
    @Inject
    private PackingRepository packingRepository;

    @Override
    public PackingDto getById(Long id) {
        return packingRepository.findById(id).map(e -> new PackingDto()).orElse(null);
    }

    @Override
    public List<PackingDto> getAll(PackingDto filter) {
        return packingRepository.findAll().list().stream().map(e -> new PackingDto()).toList();
    }
}
