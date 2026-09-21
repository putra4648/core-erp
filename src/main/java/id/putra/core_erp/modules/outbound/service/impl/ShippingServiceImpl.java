package id.putra.core_erp.modules.outbound.service.impl;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Shipment;
import id.putra.core_erp.modules.outbound.repository.ShipmentRepository;
import id.putra.core_erp.modules.outbound.service.ShippingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ShippingServiceImpl implements ShippingService {

    private final ShipmentRepository shipmentRepository;

    public ShippingServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

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
