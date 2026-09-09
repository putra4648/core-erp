package id.putra.core_erp.warehouse.module.inbound.service;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;

public interface LabellingService {
    Labelling create(Labelling dto);

    Labelling update(Labelling dto);

    Boolean delete(Long id);

    Labelling getById(Long id);

    List<Labelling> getAll(Labelling filter);
}
