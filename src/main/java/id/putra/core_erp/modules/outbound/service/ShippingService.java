package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Shipment;
import id.putra.core_erp.modules.outbound.repository.ShipmentRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ShippingService implements BaseService<Shipment> {
    private final ShipmentRepository shipmentRepository;

    public ShippingService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public void save(Shipment dto) {
        shipmentRepository.persist(dto);
    }

    public Shipment findById(Long id) {
        return shipmentRepository.findById(id);
    }

    public void update(Shipment dto) {
        shipmentRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        shipmentRepository.deleteById(id);
    }

    public List<Shipment> findAll(Shipment s, Page p) {
        return shipmentRepository.findAll().list();
    }
}
