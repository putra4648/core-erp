package id.putra.core_erp.modules.inbound.service;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.inbound.model.Receiving;
import io.quarkus.panache.common.Page;

public interface ReceivingOrderService {
    Receiving create(Receiving dto);

    Receiving update(Receiving dto);

    Boolean delete( Long id);

    DataProvider<Receiving, Void> getAll(String value, Page ofSize);

    // Receiving getById(@NonNull Long id, @NonNull Pageable pageable);

    //

}
