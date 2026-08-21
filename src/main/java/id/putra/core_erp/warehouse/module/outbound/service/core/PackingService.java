package id.putra.core_erp.warehouse.module.outbound.service.core;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.PackingDto;

public interface PackingService {
    PackingDto create(PackingDto dto);
    PackingDto update(PackingDto dto);
    Boolean delete(Long id);
    PackingDto getById(Long id);
    List<PackingDto> getAll(PackingDto filter);
}

