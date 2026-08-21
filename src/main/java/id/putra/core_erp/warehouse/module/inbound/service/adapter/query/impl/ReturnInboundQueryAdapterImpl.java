package id.putra.core_erp.warehouse.module.inbound.service.adapter.query.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.inbound.dto.ReturnDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.ReturnInboundQueryAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReturnInboundQueryAdapterImpl implements ReturnInboundQueryAdapter {
    @Override
    public ReturnDto getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public List<ReturnDto> getAll(ReturnDto filter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}

