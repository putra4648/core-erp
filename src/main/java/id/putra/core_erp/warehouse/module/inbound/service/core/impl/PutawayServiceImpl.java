package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Putaway;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.PutawayCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.PutawayQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.PutawayService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PutawayServiceImpl implements PutawayService {

    @Inject
    private PutawayCommandAdapter putawayCommandAdapter;

    @Inject
    private PutawayQueryAdapter putawayQueryAdapter;

    @Override
    public Putaway create(Putaway dto) {
        return putawayCommandAdapter.add(dto);
    }

    @Override
    public Putaway update(Putaway dto) {
        return putawayCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(Long id) {
        Putaway dto = new Putaway();
        dto.setId(id);
        return putawayCommandAdapter.delete(dto);
    }

    @Override
    public Putaway getById(Long id) {
        return putawayQueryAdapter.getById(id);
    }

    @Override
    public List<Putaway> getAll(Putaway filter) {
        return putawayQueryAdapter.getAll(filter);
    }
}
