package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;

public interface LabellingQueryAdapter {
    Labelling getById(Long id);

    List<Labelling> getAll(Labelling filter);
}
