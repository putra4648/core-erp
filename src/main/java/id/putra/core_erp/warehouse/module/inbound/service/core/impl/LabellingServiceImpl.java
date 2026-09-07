package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.LabellingDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.LabellingCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.LabellingQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.LabellingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LabellingServiceImpl implements LabellingService {

    @Inject
    private LabellingCommandAdapter labellingCommandAdapter;

    @Inject
    private LabellingQueryAdapter labellingQueryAdapter;

    @Override
    public LabellingDto create(LabellingDto dto) {
        return labellingCommandAdapter.add(dto);
    }

    @Override
    public LabellingDto update(LabellingDto dto) {
        return labellingCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(Long id) {
        LabellingDto dto = new LabellingDto();
        dto.setId(id);
        return labellingCommandAdapter.delete(dto);
    }

    @Override
    public LabellingDto getById(Long id) {
        return labellingQueryAdapter.getById(id);
    }

    @Override
    public List<LabellingDto> getAll(LabellingDto filter) {
        return labellingQueryAdapter.getAll(filter);
    }
}
