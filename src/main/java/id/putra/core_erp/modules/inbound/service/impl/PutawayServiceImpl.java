package id.putra.core_erp.modules.inbound.service.impl;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Putaway;
import id.putra.core_erp.modules.inbound.repository.PutawayRepository;
import id.putra.core_erp.modules.inbound.service.PutawayService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PutawayServiceImpl implements PutawayService {

    private final PutawayRepository putawayRepository;

    public PutawayServiceImpl(PutawayRepository putawayRepository) {
        this.putawayRepository = putawayRepository;
    }

    @Override
    public Putaway create(Putaway dto) {
        putawayRepository.persist(dto);
        return dto;
    }

    @Override
    public Putaway update(Putaway dto) {
        return putawayRepository.getEntityManager().merge(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return putawayRepository.deleteById(id);
    }

    @Override
    public Putaway getById(Long id) {
        return putawayRepository.findById(id);
    }

    @Override
    public List<Putaway> getAll(Putaway filter) {
        return putawayRepository.findAll().list();
    }
}
