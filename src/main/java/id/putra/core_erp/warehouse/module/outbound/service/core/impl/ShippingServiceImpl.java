package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.ShipmentDto;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.ShipmentCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.ShipmentQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.ShippingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShippingServiceImpl implements ShippingService {
    @Inject
    private ShipmentCommandAdapter shipmentCommandAdapter;
    @Inject
    private ShipmentQueryAdapter shipmentQueryAdapter;

    @Override
    public ShipmentDto create(ShipmentDto dto) {
        shipmentCommandAdapter.add(List.of(dto));
        return dto;
    }

    @Override
    public ShipmentDto update(ShipmentDto dto) {
        shipmentCommandAdapter.update(List.of(dto));
        return dto;
    }

    @Override
    public Boolean delete(Long id) {
        ShipmentDto dto = new ShipmentDto();
        dto.setId(id);
        shipmentCommandAdapter.delete(List.of(dto));
        return true;
    }

    @Override
    public ShipmentDto getById(Long id) {
        return shipmentQueryAdapter.getById(id);
    }

    @Override
    public List<ShipmentDto> getAll(ShipmentDto filter) {
        return shipmentQueryAdapter.getAll(filter);
    }
}
