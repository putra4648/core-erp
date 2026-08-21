package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.outbound.converter.ShipmentConverter;
import id.putra.core_erp.warehouse.module.outbound.dto.ShipmentDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.ShipmentRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.ShipmentCommandAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentCommandAdapterImpl implements ShipmentCommandAdapter {
    private final ShipmentRepository shipmentRepository;

    @Override
    public void add(List<ShipmentDto> dtos) {
        var entities = dtos.stream().map(ShipmentConverter::toEntity).toList();
        shipmentRepository.saveAll(entities);
    }

    @Override
    public void update(List<ShipmentDto> dtos) {
        var entities = dtos.stream().map(ShipmentConverter::toEntity).toList();
        shipmentRepository.saveAll(entities);
    }

    @Override
    public void delete(List<ShipmentDto> dtos) {
        var ids = dtos.stream().map(item -> item.getId()).toList();
        shipmentRepository.deleteAllById(ids);
    }
}

