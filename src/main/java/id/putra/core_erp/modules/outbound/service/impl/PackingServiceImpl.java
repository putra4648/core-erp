package id.putra.core_erp.modules.outbound.service.impl;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Packing;
import id.putra.core_erp.modules.outbound.repository.PackingRepository;
import id.putra.core_erp.modules.outbound.service.PackingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PackingServiceImpl implements PackingService {

    private final PackingRepository packingRepository;

    public PackingServiceImpl(PackingRepository packingRepository) {
        this.packingRepository = packingRepository;
    }

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
