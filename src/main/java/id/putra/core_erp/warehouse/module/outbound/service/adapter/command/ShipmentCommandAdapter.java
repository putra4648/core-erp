package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;

public interface ShipmentCommandAdapter {
    void add(List<Shipment> dtos);

    void update(List<Shipment> dtos);

    void delete(List<Long> ids);
}
