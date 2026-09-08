package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.ShipmentDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.ShipmentRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.ShipmentQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipmentQueryAdapterImpl implements ShipmentQueryAdapter {
    private ShipmentRepository shipmentRepository;

    @Override
    public ShipmentDto getById(Long id) {
        return shipmentRepository.findById(id).map(e -> new ShipmentDto()).orElse(null);
    }

    @Override
    public List<ShipmentDto> getAll(ShipmentDto filter) {
        return shipmentRepository.findAll().list().stream().map(e -> new ShipmentDto()).toList();
    }
}
