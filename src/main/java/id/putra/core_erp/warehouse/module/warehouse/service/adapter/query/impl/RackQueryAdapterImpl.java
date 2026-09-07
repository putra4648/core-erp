package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.warehouse.dto.RackDto;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.RackRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.RackQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RackQueryAdapterImpl implements RackQueryAdapter {

    @Inject
    private RackRepository rackRepository;

    @Override
    public RackDto getRackById(RackDto dto) {

        var r = rackRepository.findById(dto.getId());
        return null;
    }

}
