package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;
import id.putra.core_erp.warehouse.module.outbound.model.repository.ShipmentRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.ShipmentCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShipmentCommandAdapterImpl implements ShipmentCommandAdapter {
    @Inject
    private ShipmentRepository shipmentRepository;

    @Override
    public void add(List<Shipment> dtos) {
    }

    @Override
    public void update(List<Shipment> dtos) {
    }

    @Override
    public void delete(List<Long> ids) {

    }
}
