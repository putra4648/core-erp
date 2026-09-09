package id.putra.core_erp.warehouse.module.outbound.service;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;

public interface ShippingService {
    void create(Shipment dto);

    void update(Shipment dto);

    void delete(Long id);

    Shipment getById(Long id);

    List<Shipment> getAll(Shipment filter);
}
