package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Shipment;

public interface ShippingService {
    void create(Shipment dto);

    void update(Shipment dto);

    void delete(Long id);

    Shipment getById(Long id);

    List<Shipment> getAll(Shipment filter);
}
