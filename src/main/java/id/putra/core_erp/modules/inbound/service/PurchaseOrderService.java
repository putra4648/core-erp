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

<<<<<<< HEAD
	public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	@Override
	public void delete(Long id) {
		purchaseOrderRepository.deleteById(id);
	}

	@Override
	public List<PurchaseOrder> findAll(PurchaseOrder po, Page p) {
		return purchaseOrderRepository.findAll().list();
	}

	@Override
	public PurchaseOrder findById(Long id) {
		return purchaseOrderRepository.findById(id);
	}

	@Override
	public void save(PurchaseOrder entity) {
		purchaseOrderRepository.persist(entity);
=======
	private final PurchaseOrderRepository purchaseOrderRepository;

	public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	@Override
	public void save(PurchaseOrder entity) {
		purchaseOrderRepository.persist(entity);
	}

	public PurchaseOrder create(PurchaseOrder dto) {
		purchaseOrderRepository.persist(dto);
		return dto;
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
	public Iterable<PurchaseOrder> findAll(PurchaseOrder po, Page p) {
		return purchaseOrderRepository.findAll().list();
	}

	public List<PurchaseOrder> getAll(PurchaseOrder dto, Page page) {
		String poNumber = dto.getPoNumber() == null ? "" : dto.getPoNumber();
		return purchaseOrderRepository.find("poNumber LIKE ?1", "%" + poNumber + "%").page(page).list();
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public void update(PurchaseOrder dto) {
		purchaseOrderRepository.getEntityManager().merge(dto);
	}
}
