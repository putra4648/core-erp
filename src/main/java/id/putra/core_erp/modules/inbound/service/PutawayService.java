package id.putra.core_erp.modules.inbound.service;

import id.putra.core_erp.service.BaseService;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Putaway;
import id.putra.core_erp.modules.inbound.repository.PutawayRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PutawayService implements BaseService<Putaway> {

    private final PutawayRepository putawayRepository;

    public PutawayService(PutawayRepository putawayRepository) {
        this.putawayRepository = putawayRepository;
    }

    public Putaway save(Putaway entity) {
        putawayRepository.persist(entity);
        return entity;
    }

    public Putaway create(Putaway dto) {
        putawayRepository.persist(dto);
        return dto;
    }

    public void update(Putaway dto) {
        putawayRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        putawayRepository.deleteById(id);
    }

    public Putaway findById(Long id) {
        return putawayRepository.findById(id);
    }

    public Putaway getById(Long id) {
        return findById(id);
    }

    public Iterable<Putaway> findAll() {
        return putawayRepository.findAll().list();
    }

    public List<Putaway> getAll(Putaway filter) {
        return putawayRepository.findAll().list();
    }
}
