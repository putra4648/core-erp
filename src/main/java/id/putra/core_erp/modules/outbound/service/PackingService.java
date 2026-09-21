package id.putra.core_erp.modules.outbound.service;

import id.putra.core_erp.service.BaseService;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Packing;
import id.putra.core_erp.modules.outbound.repository.PackingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PackingService implements BaseService<Packing> {

    private final PackingRepository packingRepository;

    public PackingService(PackingRepository packingRepository) {
        this.packingRepository = packingRepository;
    }

    public Packing save(Packing dto) {
        packingRepository.persist(dto);
        return dto;
    }

    public Packing create(Packing dto) {
        return save(dto);
    }

    public void update(Packing dto) {
        packingRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        packingRepository.deleteById(id);
    }

    public Packing getById(Long id) {
        return packingRepository.findById(id);
    }

    public Packing findById(Long id) {
        return packingRepository.findById(id);
    }

    public Iterable<Packing> findAll() {
        return packingRepository.findAll().list();
    }

    public List<Packing> getAll(Packing filter) {
        return packingRepository.findAll().list();
    }
}
