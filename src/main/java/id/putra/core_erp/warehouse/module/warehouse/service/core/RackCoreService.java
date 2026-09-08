package id.putra.core_erp.warehouse.module.warehouse.service.core;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Rack;
import io.quarkus.panache.common.Page;

public interface RackCoreService {

    Rack getRack(Rack rackDto);

    void add(List<Rack> dtos);

    void update(List<Rack> dtos);

    void delete(List<Rack> dtos);

    DataProvider<Rack, Void> getRacks(Rack param, Page ofSize);
}
