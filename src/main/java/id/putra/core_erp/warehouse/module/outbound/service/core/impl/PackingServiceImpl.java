package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Packing;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PackingRepository;
import id.putra.core_erp.warehouse.module.outbound.service.core.PackingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PackingServiceImpl implements PackingService {
    @Inject
    private PackingRepository packingRepository;

    @Override
    public void create(Packing dto) {
        packingRepository.persist(dto);
    }

    @Override
    public void update(Packing dto) {
        packingRepository.getEntityManager().merge(dto);
    }

    @Override
    public void delete(Long id) {
        packingRepository.deleteById(id);
    }

    @Override
    public Packing getById(Long id) {
        return packingRepository.findById(id);
    }

    @Override
    public List<Packing> getAll(Packing filter) {
        return packingRepository.findAll().list();
    }
}
