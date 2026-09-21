package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Packing;

public interface PackingService {
    void create(Packing dto);

    void update(Packing dto);

    void delete(Long id);

    Packing getById(Long id);

    List<Packing> getAll(Packing filter);
}
