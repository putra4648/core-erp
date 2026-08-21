package id.putra.core_erp.warehouse.module.outbound.service.core;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;

public interface ReturnOutboundService {
    SalesOrderDto create(SalesOrderDto dto);
    SalesOrderDto update(SalesOrderDto dto);
    Boolean delete(Long id);
    SalesOrderDto getById(Long id);
    List<SalesOrderDto> getAll(SalesOrderDto filter);
}

