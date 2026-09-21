package id.putra.core_erp.modules.inbound.service;

import id.putra.core_erp.shared.service.BaseService;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Labelling;
import id.putra.core_erp.modules.inbound.repository.LabellingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LabellingService implements BaseService<Labelling> {

    private final LabellingRepository labellingRepository;

    public LabellingService(LabellingRepository labellingRepository) {
        this.labellingRepository = labellingRepository;
    }
    public Labelling save(Labelling entity) {
        labellingRepository.persist(entity);
        return entity;
    }
    public Labelling create(Labelling dto) {
        labellingRepository.persist(dto);
        return dto;
    }
    public void update(Labelling dto) {
        labellingRepository.getEntityManager().merge(dto);
    }
    public void delete(Long id) {
        labellingRepository.deleteById(id);
    }
    public Labelling findById(Long id) {
        return labellingRepository.findById(id);
    }

    public Labelling getById(Long id) {
        return findById(id);
    }
    public Iterable<Labelling> findAll() {
        return labellingRepository.findAll().list();
    }
    public List<Labelling> getAll(Labelling filter) {
        return labellingRepository.findAll().list();
    }
}
