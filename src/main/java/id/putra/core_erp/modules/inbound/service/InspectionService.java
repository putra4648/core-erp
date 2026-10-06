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

<<<<<<< HEAD
	public InspectionService(QualityInspectionRepository qualityInspectionRepository) {
		this.qualityInspectionRepository = qualityInspectionRepository;
	}

	@Override
	public void delete(Long id) {
		qualityInspectionRepository.deleteById(id);
	}

	@Override
	public List<QualityInspection> findAll(QualityInspection qi, Page page) {
		return qualityInspectionRepository.findAll().list();
	}

	@Override
	public QualityInspection findById(Long id) {
		return qualityInspectionRepository.findById(id);
	}

	@Override
	public void save(QualityInspection entity) {
		qualityInspectionRepository.persist(entity);
	}

	@Override
	public void update(QualityInspection dto) {
		qualityInspectionRepository.getEntityManager().merge(dto);
=======
	private final QualityInspectionRepository qualityInspectionRepository;

	public InspectionService(QualityInspectionRepository qualityInspectionRepository) {
		this.qualityInspectionRepository = qualityInspectionRepository;
	}

	@Override
	public void save(QualityInspection entity) {
		qualityInspectionRepository.persist(entity);
	}

	public QualityInspection create(QualityInspection dto) {
		qualityInspectionRepository.persist(dto);
		return dto;
	}

	@Override
	public void update(QualityInspection dto) {
		qualityInspectionRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		qualityInspectionRepository.deleteById(id);
	}

	@Override
	public QualityInspection findById(Long id) {
		return qualityInspectionRepository.findById(id);
	}

	public QualityInspection getById(Long id) {
		return findById(id);
	}

	@Override
	public Iterable<QualityInspection> findAll(QualityInspection qi, Page page) {
		return qualityInspectionRepository.findAll().list();
	}

	public List<QualityInspection> getAll(QualityInspection filter) {
		return qualityInspectionRepository.findAll().list();
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
