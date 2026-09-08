package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;

public interface LabellingCommandAdapter {
    Labelling add(Labelling dto);

    Labelling update(Labelling dto);

    Boolean delete(Labelling dto);
}
