package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Carrier;
import id.putra.core_erp.warehouse.module.outbound.model.repository.CarrierRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.CarrierQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CarrierQueryAdapterImpl implements CarrierQueryAdapter {
    @Inject
    private CarrierRepository carrierRepository;

    @Override
    public Carrier getById(Long id) {
        return carrierRepository.findById(id);
    }

    @Override
    public List<Carrier> getAll(Carrier filter) {
        return carrierRepository.findAll().list();
    }
}
