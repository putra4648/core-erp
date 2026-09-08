package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Return;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.ReturnInboundCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.ReturnInboundQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.ReturnInboundService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReturnInboundServiceImpl implements ReturnInboundService {

    @Inject
    private ReturnInboundCommandAdapter returnInboundCommandAdapter;
    @Inject
    private ReturnInboundQueryAdapter returnInboundQueryAdapter;

    @Override
    public Return create(Return dto) {
        return returnInboundCommandAdapter.add(dto);
    }

    @Override
    public Return update(Return dto) {
        return returnInboundCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(Long id) {
        Return dto = new Return();
        dto.setId(id);
        return returnInboundCommandAdapter.delete(dto);
    }

    @Override
    public Return getById(Long id) {
        return returnInboundQueryAdapter.getById(id);
    }

    @Override
    public List<Return> getAll(Return filter) {
        return returnInboundQueryAdapter.getAll(filter);
    }
}
