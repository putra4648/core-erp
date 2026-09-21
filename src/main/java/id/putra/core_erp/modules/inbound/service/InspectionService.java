package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.QualityInspection;
import id.putra.core_erp.modules.inbound.repository.QualityInspectionRepository;
import id.putra.core_erp.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InspectionService implements BaseService<QualityInspection> {

    private final QualityInspectionRepository qualityInspectionRepository;

    public InspectionService(QualityInspectionRepository qualityInspectionRepository) {
        this.qualityInspectionRepository = qualityInspectionRepository;
    }

    public QualityInspection save(QualityInspection entity) {
        qualityInspectionRepository.persist(entity);
        return entity;
    }

    public QualityInspection create(QualityInspection dto) {
        qualityInspectionRepository.persist(dto);
        return dto;
    }

    public void update(QualityInspection dto) {
        qualityInspectionRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        qualityInspectionRepository.deleteById(id);
    }

    public QualityInspection findById(Long id) {
        return qualityInspectionRepository.findById(id);
    }

    public QualityInspection getById(Long id) {
        return findById(id);
    }

    public Iterable<QualityInspection> findAll() {
        return qualityInspectionRepository.findAll().list();
    }

    public List<QualityInspection> getAll(QualityInspection filter) {
        return qualityInspectionRepository.findAll().list();
    }
}
