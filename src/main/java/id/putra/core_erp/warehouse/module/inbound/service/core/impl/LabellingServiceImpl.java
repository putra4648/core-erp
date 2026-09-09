package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;
import id.putra.core_erp.warehouse.module.inbound.model.repository.LabellingRepository;
import id.putra.core_erp.warehouse.module.inbound.service.core.LabellingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LabellingServiceImpl implements LabellingService {

    @Inject
    private LabellingRepository labellingRepository;

    @Override
    public Labelling create(Labelling dto) {
        labellingRepository.persist(dto);
        return dto;
    }

    @Override
    public Labelling update(Labelling dto) {
        return labellingRepository.getEntityManager().merge(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return labellingRepository.deleteById(id);
    }

    @Override
    public Labelling getById(Long id) {
        return labellingRepository.findById(id);
    }

    @Override
    public List<Labelling> getAll(Labelling filter) {
        return labellingRepository.findAll().list();
    }
}
