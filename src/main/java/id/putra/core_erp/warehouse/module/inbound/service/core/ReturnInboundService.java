package id.putra.core_erp.warehouse.module.inbound.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Return;

public interface ReturnInboundService {
    Return create(Return dto);

    Return update(Return dto);

    Boolean delete(Long id);

    Return getById(Long id);

    List<Return> getAll(Return filter);
}
