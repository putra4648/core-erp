package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.model.entity.PurchaseOrder;
import id.putra.core_erp.warehouse.module.inbound.model.repository.PurchaseOrderRepository;
import id.putra.core_erp.warehouse.module.inbound.service.core.PurchaseOrderService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Inject
    private PurchaseOrderRepository purchaseOrderRepository;

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
    public Boolean delete(@NonNull Long id) {
        return purchaseOrderRepository.deleteById(id);
    }

    @Override
    public List<PurchaseOrder> getAll(PurchaseOrder dto, Page page) {
        String poNumber = dto.getPoNumber() == null ? "" : dto.getPoNumber();
        return purchaseOrderRepository.find("poNumber LIKE ?1", "%" + poNumber + "%").page(page).list();
    }

}
