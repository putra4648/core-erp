package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;

public interface ShipmentQueryAdapter {
    Shipment getById(Long id);

    List<Shipment> getAll(Shipment filter);
}
