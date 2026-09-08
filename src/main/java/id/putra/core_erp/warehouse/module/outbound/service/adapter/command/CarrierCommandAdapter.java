package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Carrier;

public interface CarrierCommandAdapter {
    void add(List<Carrier> dtos);

    void update(List<Carrier> dtos);

    void delete(List<Carrier> dtos);
}
