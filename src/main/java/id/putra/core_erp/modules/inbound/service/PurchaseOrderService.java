package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.PurchaseOrder;
import id.putra.core_erp.modules.inbound.repository.PurchaseOrderRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PurchaseOrderService implements BaseService<PurchaseOrder> {
	private final PurchaseOrderRepository purchaseOrderRepository;

	public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	@Override
	public void save(PurchaseOrder entity) {
		purchaseOrderRepository.persist(entity);
	}

	@Override
	public void update(PurchaseOrder dto) {
		purchaseOrderRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		purchaseOrderRepository.deleteById(id);
	}

	@Override
	public PurchaseOrder findById(Long id) {
		return purchaseOrderRepository.findById(id);
	}

	@Override
	public List<PurchaseOrder> findAll(PurchaseOrder po, Page p) {
		return purchaseOrderRepository.findAll().list();
	}

}
