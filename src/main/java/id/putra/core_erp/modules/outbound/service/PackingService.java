package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Packing;
import id.putra.core_erp.modules.outbound.repository.PackingRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PackingService implements BaseService<Packing> {
    private final PackingRepository packingRepository;

    public PackingService(PackingRepository packingRepository) {
        this.packingRepository = packingRepository;
    }

    public void save(Packing dto) {
        packingRepository.persist(dto);
    }

    public Packing findById(Long id) {
        return packingRepository.findById(id);
    }

    public void update(Packing dto) {
        packingRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        packingRepository.deleteById(id);
    }

    public List<Packing> findAll(Packing packing, Page p) {
        return packingRepository.findAll().list();
    }
}
