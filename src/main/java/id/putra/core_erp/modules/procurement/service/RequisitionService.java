package id.putra.core_erp.modules.procurement.service;

import java.util.List;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(rollbackOn = { Exception.class })
public class RequisitionService implements BaseService<Requisition> {

	private final RequisitionRepo requisitionRepo;

	public RequisitionService(RequisitionRepo requisitionRepo) {
		this.requisitionRepo = requisitionRepo;
	}

	@Override
	public void save(Requisition entity) {
		requisitionRepo.persist(entity);
	}

	@Override
	public void update(Requisition entity) {
		requisitionRepo.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		requisitionRepo.deleteById(id);
	}

	@Override
	public Requisition findById(Long id) {
		return requisitionRepo.findById(id);
	}

	@Override
	public List<Requisition> findAll(Requisition r, Page p) {
		return requisitionRepo.findAll().list();
	}

}
