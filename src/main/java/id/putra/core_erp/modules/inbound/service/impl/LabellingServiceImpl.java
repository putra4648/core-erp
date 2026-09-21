package id.putra.core_erp.modules.inbound.service.impl;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Labelling;
import id.putra.core_erp.modules.inbound.repository.LabellingRepository;
import id.putra.core_erp.modules.inbound.service.LabellingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LabellingServiceImpl implements LabellingService {

    private final LabellingRepository labellingRepository;

    public LabellingServiceImpl(LabellingRepository labellingRepository) {
        this.labellingRepository = labellingRepository;
    }

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
