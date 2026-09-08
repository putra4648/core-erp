package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Return;

public interface ReturnInboundQueryAdapter {
    Return getById(Long id);

    List<Return> getAll(Return filter);
}
