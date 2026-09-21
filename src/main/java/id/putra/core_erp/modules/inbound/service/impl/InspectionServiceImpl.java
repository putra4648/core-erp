package id.putra.core_erp.modules.inbound.service.impl;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.QualityInspection;
import id.putra.core_erp.modules.inbound.repository.QualityInspectionRepository;
import id.putra.core_erp.modules.inbound.service.InspectionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InspectionServiceImpl implements InspectionService {

    private final QualityInspectionRepository qualityInspectionRepository;

    public InspectionServiceImpl(QualityInspectionRepository qualityInspectionRepository) {
        this.qualityInspectionRepository = qualityInspectionRepository;
    }

    @Override
    public QualityInspection create(QualityInspection dto) {
        qualityInspectionRepository.persist(dto);
        return dto;
    }

    @Override
    public QualityInspection update(QualityInspection dto) {
        return qualityInspectionRepository.getEntityManager().merge(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return qualityInspectionRepository.deleteById(id);
    }

    @Override
    public QualityInspection getById(Long id) {
        return qualityInspectionRepository.findById(id);
    }

    @Override
    public List<QualityInspection> getAll(QualityInspection filter) {
        return qualityInspectionRepository.findAll().list();
    }
}
