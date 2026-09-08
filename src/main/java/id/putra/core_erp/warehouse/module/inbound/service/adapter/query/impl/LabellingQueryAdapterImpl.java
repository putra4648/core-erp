package id.putra.core_erp.warehouse.module.inbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.LabellingQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LabellingQueryAdapterImpl implements LabellingQueryAdapter {
    @Override
    public Labelling getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public List<Labelling> getAll(Labelling filter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
