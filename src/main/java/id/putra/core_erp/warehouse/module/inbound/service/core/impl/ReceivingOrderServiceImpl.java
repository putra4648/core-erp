package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Receiving;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.ReceivingCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.ReceivingQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.ReceivingOrderService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReceivingOrderServiceImpl implements ReceivingOrderService {

    @Inject
    private ReceivingCommandAdapter receivingCommandAdapter;

    @Inject
    private ReceivingQueryAdapter receivingQueryAdapter;

    @Override
    public Receiving create(Receiving dto) {
        return receivingCommandAdapter.add(dto);
    }

    @Override
    public Receiving update(Receiving dto) {
        return receivingCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(@NonNull Long id) {
        return receivingCommandAdapter.delete(id);
    }

    @Override
    public DataProvider<Receiving, Void> getAll(String value, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
