package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.ZoneDto;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.ZoneRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.ZoneCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ZoneCommandAdapterImpl implements ZoneCommandAdapter {

    @Inject
    private ZoneRepository zoneRepository;

    @Override
    public void add(List<ZoneDto> dtos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void update(List<ZoneDto> dtos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(List<ZoneDto> dtos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
