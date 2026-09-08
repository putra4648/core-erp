package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Carrier;
import id.putra.core_erp.warehouse.module.outbound.model.repository.CarrierRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.CarrierCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CarrierCommandAdapterImpl implements CarrierCommandAdapter {
    @Inject
    private CarrierRepository carrierRepository;

    @Override
    public void add(List<Carrier> dtos) {

    }

    @Override
    public void update(List<Carrier> dtos) {
    }

    @Override
    public void delete(List<Carrier> dtos) {
    }
}
