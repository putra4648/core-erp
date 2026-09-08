package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.LabellingCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LabellingCommandAdapterImpl implements LabellingCommandAdapter {

    @Override
    public Labelling add(Labelling dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Labelling update(Labelling dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Boolean delete(Labelling dto) {
        // TODO: Placeholder - implement when repository is ready
        return false;
    }
}
