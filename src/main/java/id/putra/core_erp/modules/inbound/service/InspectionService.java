package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.QualityInspection;
import id.putra.core_erp.modules.inbound.repository.QualityInspectionRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InspectionService implements BaseService<QualityInspection> {
    private final QualityInspectionRepository qualityInspectionRepository;

    public InspectionService(QualityInspectionRepository qualityInspectionRepository) {
        this.qualityInspectionRepository = qualityInspectionRepository;
    }

    public void save(QualityInspection entity) {
        qualityInspectionRepository.persist(entity);
    }

    public QualityInspection findById(Long id) {
        return qualityInspectionRepository.findById(id);
    }

    public void update(QualityInspection dto) {
        qualityInspectionRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        qualityInspectionRepository.deleteById(id);
    }

    public List<QualityInspection> findAll(QualityInspection qi, Page page) {
        return qualityInspectionRepository.findAll().list();
    }
}
