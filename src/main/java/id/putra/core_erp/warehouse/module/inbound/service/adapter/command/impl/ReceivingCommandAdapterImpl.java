package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.inbound.converter.ReceivingConverter;
import id.putra.core_erp.warehouse.module.inbound.dto.ReceivingDto;
import id.putra.core_erp.warehouse.module.inbound.model.repository.PurchaseOrderRepository;
import id.putra.core_erp.warehouse.module.inbound.model.repository.ReceivingRepository;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.ReceivingCommandAdapter;
import id.putra.core_erp.warehouse.shared.enums.OrderStatus;
import id.putra.core_erp.warehouse.shared.enums.ResponseEnum;
import id.putra.core_erp.warehouse.shared.exceptions.ModuleException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ReceivingCommandAdapterImpl implements ReceivingCommandAdapter {

    private final ReceivingRepository receivingRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public ReceivingDto add(ReceivingDto dto) {
        var po = purchaseOrderRepository.findById(Objects.requireNonNull(dto.getPurchaseOrder().getId()))
                .orElseThrow(() -> new ModuleException(ResponseEnum.DATA_NOT_FOUND));
        po.setStatus(OrderStatus.PENDING);
        purchaseOrderRepository.save(po);

        var entity = ReceivingConverter.toEntity(dto);
        return ReceivingConverter.toDto(receivingRepository.save(Objects.requireNonNull(entity)));
    }

    @Override
    public ReceivingDto update(ReceivingDto dto) {
        var entity = ReceivingConverter.toEntity(dto);
        return ReceivingConverter.toDto(receivingRepository.save(Objects.requireNonNull(entity)));
    }

    @Override
    public Boolean delete(Long id) {
        if (id == null) {
            throw new ModuleException(ResponseEnum.INVALID_PARAM);
        }
        if (receivingRepository.existsById(id)) {
            receivingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ReceivingDto patch(ReceivingDto body) {
        var entity = receivingRepository.findById(Objects.requireNonNull(body.getId()))
                .orElseThrow(() -> new ModuleException(ResponseEnum.DATA_NOT_FOUND));
        ReceivingConverter.patchDtoToEntity(body, entity);
        return ReceivingConverter.toDto(receivingRepository.save(Objects.requireNonNull(entity)));
    }
}

