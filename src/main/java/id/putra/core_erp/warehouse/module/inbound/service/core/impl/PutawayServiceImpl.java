package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Putaway;
import id.putra.core_erp.warehouse.module.inbound.model.repository.PutawayRepository;
import id.putra.core_erp.warehouse.module.inbound.service.core.PutawayService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PutawayServiceImpl implements PutawayService {

    @Inject
    private PutawayRepository putawayRepository;

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
