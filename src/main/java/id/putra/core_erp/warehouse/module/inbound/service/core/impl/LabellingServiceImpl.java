package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.LabellingCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.LabellingQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.LabellingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LabellingServiceImpl implements LabellingService {

    @Inject
    private LabellingCommandAdapter labellingCommandAdapter;

    @Inject
    private LabellingQueryAdapter labellingQueryAdapter;

    @Override
    public Labelling create(Labelling dto) {
        return labellingCommandAdapter.add(dto);
    }

    @Override
    public Labelling update(Labelling dto) {
        return labellingCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(Long id) {
        Labelling dto = new Labelling();
        dto.setId(id);
        return labellingCommandAdapter.delete(dto);
    }

    @Override
    public Labelling getById(Long id) {
        return labellingQueryAdapter.getById(id);
    }

    @Override
    public List<Labelling> getAll(Labelling filter) {
        return labellingQueryAdapter.getAll(filter);
    }
}
