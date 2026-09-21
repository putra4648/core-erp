package id.putra.core_erp.modules.supplier.service;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.supplier.model.Supplier;
import io.quarkus.panache.common.Page;

public interface SupplierService {

    Supplier getById(Long id);

    List<Supplier> create(List<Supplier> dto);

    void update(Long id, Supplier dto);

    void delete(Long id);

    DataProvider<Supplier, Void> getAll(String value, Page ofSize);
}
