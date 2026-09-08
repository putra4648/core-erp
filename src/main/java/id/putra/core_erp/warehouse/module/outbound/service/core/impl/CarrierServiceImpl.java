package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Carrier;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.CarrierCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.CarrierQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.CarrierService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CarrierServiceImpl implements CarrierService {
    @Inject
    private CarrierCommandAdapter carrierCommandAdapter;
    @Inject
    private CarrierQueryAdapter carrierQueryAdapter;

    @Override
    public Carrier create(Carrier dto) {
        carrierCommandAdapter.add(List.of(dto));
        return dto;
    }

    @Override
    public Carrier update(Carrier dto) {
        carrierCommandAdapter.update(List.of(dto));
        return dto;
    }

    @Override
    public Boolean delete(Long id) {
        Carrier dto = new Carrier();
        dto.setId(id);
        carrierCommandAdapter.delete(List.of(dto));
        return true;
    }

    @Override
    public Carrier getById(Long id) {
        return carrierQueryAdapter.getById(id);
    }

    @Override
    public List<Carrier> getAll(Carrier filter) {
        return carrierQueryAdapter.getAll(filter);
    }
}
