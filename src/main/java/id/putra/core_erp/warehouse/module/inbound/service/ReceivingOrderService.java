package id.putra.core_erp.warehouse.module.inbound.service;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Receiving;
import io.quarkus.panache.common.Page;

public interface ReceivingOrderService {
    Receiving create(Receiving dto);

    Receiving update(Receiving dto);

    Boolean delete(@NonNull Long id);

    DataProvider<Receiving, Void> getAll(String value, Page ofSize);

    // Receiving getById(@NonNull Long id, @NonNull Pageable pageable);

    //

}
