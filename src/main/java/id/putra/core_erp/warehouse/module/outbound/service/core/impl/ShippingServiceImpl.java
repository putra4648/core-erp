package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.ShipmentCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.ShipmentQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.ShippingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShippingServiceImpl implements ShippingService {
    @Inject
    private ShipmentCommandAdapter shipmentCommandAdapter;
    @Inject
    private ShipmentQueryAdapter shipmentQueryAdapter;

    @Override
    public void create(Shipment dto) {
        shipmentCommandAdapter.add(List.of(dto));
    }

    @Override
    public void update(Shipment dto) {
        shipmentCommandAdapter.update(List.of(dto));
    }

    @Override
    public void delete(Long id) {
        shipmentCommandAdapter.delete(List.of(id));
    }

    @Override
    public Shipment getById(Long id) {
        return shipmentQueryAdapter.getById(id);
    }

    @Override
    public List<Shipment> getAll(Shipment filter) {
        return shipmentQueryAdapter.getAll(filter);
    }
}
