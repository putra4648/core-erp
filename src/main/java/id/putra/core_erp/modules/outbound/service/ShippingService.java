package id.putra.core_erp.modules.outbound.service;

import id.putra.core_erp.service.BaseService;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Shipment;
import id.putra.core_erp.modules.outbound.repository.ShipmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ShippingService implements BaseService<Shipment> {

    private final ShipmentRepository shipmentRepository;

    public ShippingService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Shipment save(Shipment dto) {
        shipmentRepository.persist(dto);
        return dto;
    }

    public Shipment create(Shipment dto) {
        return save(dto);
    }

    public void update(Shipment dto) {
        shipmentRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        shipmentRepository.deleteById(id);
    }

    public Shipment getById(Long id) {
        return shipmentRepository.findById(id);
    }

    public Shipment findById(Long id) {
        return shipmentRepository.findById(id);
    }

    public Iterable<Shipment> findAll() {
        return shipmentRepository.findAll().list();
    }

    public List<Shipment> getAll(Shipment filter) {
        return shipmentRepository.findAll().list();
    }
}
