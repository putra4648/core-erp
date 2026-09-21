package id.putra.core_erp.modules.inbound.service.impl;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.PurchaseOrder;
import id.putra.core_erp.modules.inbound.repository.PurchaseOrderRepository;
import id.putra.core_erp.modules.inbound.service.PurchaseOrderService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public PurchaseOrder create(PurchaseOrder dto) {
        purchaseOrderRepository.persist(dto);
        return dto;
    }

    @Override
    public PurchaseOrder update(PurchaseOrder dto) {
        return purchaseOrderRepository.getEntityManager().merge(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return purchaseOrderRepository.deleteById(id);
    }

    @Override
    public List<PurchaseOrder> getAll(PurchaseOrder dto, Page page) {
        String poNumber = dto.getPoNumber() == null ? "" : dto.getPoNumber();
        return purchaseOrderRepository.find("poNumber LIKE ?1", "%" + poNumber + "%").page(page).list();
    }

}
