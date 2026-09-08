package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;
import id.putra.core_erp.warehouse.module.outbound.model.repository.ShipmentRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.ShipmentQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipmentQueryAdapterImpl implements ShipmentQueryAdapter {
    private ShipmentRepository shipmentRepository;

    @Override
    public Shipment getById(Long id) {
        return shipmentRepository.findById(id);
    }

    @Override
    public List<Shipment> getAll(Shipment filter) {
        return shipmentRepository.findAll().list();
    }
}
