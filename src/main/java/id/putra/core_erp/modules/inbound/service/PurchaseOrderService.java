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

    public PurchaseOrder save(PurchaseOrder entity) {
        purchaseOrderRepository.persist(entity);
        return entity;
    }

    public PurchaseOrder create(PurchaseOrder dto) {
        purchaseOrderRepository.persist(dto);
        return dto;
    }

    public void update(PurchaseOrder dto) {
        purchaseOrderRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    public PurchaseOrder findById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    public Iterable<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll().list();
    }

    public List<PurchaseOrder> getAll(PurchaseOrder dto, Page page) {
        String poNumber = dto.getPoNumber() == null ? "" : dto.getPoNumber();
        return purchaseOrderRepository.find("poNumber LIKE ?1", "%" + poNumber + "%").page(page).list();
    }

}
