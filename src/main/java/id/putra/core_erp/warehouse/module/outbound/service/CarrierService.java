package id.putra.core_erp.warehouse.module.outbound.service;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Carrier;

public interface CarrierService {
    Carrier create(Carrier dto);

    Carrier update(Carrier dto);

    Boolean delete(Long id);

    Carrier getById(Long id);

    List<Carrier> getAll(Carrier filter);
}
