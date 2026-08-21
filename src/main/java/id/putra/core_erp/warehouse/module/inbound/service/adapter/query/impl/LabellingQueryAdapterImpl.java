package id.putra.core_erp.warehouse.module.inbound.service.adapter.query.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.inbound.dto.LabellingDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.LabellingQueryAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabellingQueryAdapterImpl implements LabellingQueryAdapter {
    @Override
    public LabellingDto getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public List<LabellingDto> getAll(LabellingDto filter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}

