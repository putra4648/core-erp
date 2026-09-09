package id.putra.core_erp.warehouse.module.inbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Putaway;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.PutawayQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PutawayQueryAdapterImpl implements PutawayQueryAdapter {
    @Override
    public Putaway getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public List<Putaway> getAll(Putaway filter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
