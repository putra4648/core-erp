package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;
import id.putra.core_erp.warehouse.module.outbound.model.repository.ShipmentRepository;
import id.putra.core_erp.warehouse.module.outbound.service.core.ShippingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ShippingServiceImpl implements ShippingService {
    @Inject
    private ShipmentRepository shipmentRepository;

    @Override
    public void create(Shipment dto) {
        shipmentRepository.persist(dto);
    }

    @Override
    public void update(Shipment dto) {
        shipmentRepository.getEntityManager().merge(dto);
    }

    @Override
    public void delete(Long id) {
        shipmentRepository.deleteById(id);
    }

    @Override
    public Shipment getById(Long id) {
        return shipmentRepository.findById(id);
    }

    @Override
    public List<Shipment> getAll(Shipment filter) {
        return shipmentRepository.findAll().list();
    }
}
