package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.SalesOrder;

public interface ReturnOutboundService {
    SalesOrder create(SalesOrder dto);

    SalesOrder update(SalesOrder dto);

    Boolean delete(Long id);

    SalesOrder getById(Long id);

    List<SalesOrder> getAll(SalesOrder filter);
}
