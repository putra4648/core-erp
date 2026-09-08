package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Carrier;

public interface CarrierQueryAdapter {
    Carrier getById(Long id);

    List<Carrier> getAll(Carrier filter);
}
