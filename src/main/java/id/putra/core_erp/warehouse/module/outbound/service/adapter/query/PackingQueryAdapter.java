package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.PackingDto;

public interface PackingQueryAdapter {
    PackingDto getById(Long id);
    List<PackingDto> getAll(PackingDto filter);
}

