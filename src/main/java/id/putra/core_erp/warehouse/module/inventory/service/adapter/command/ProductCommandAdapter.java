package id.putra.core_erp.warehouse.module.inventory.service.adapter.command;

import id.putra.core_erp.shared.entity.Product;

public interface ProductCommandAdapter {

    void save(Product dto);

    void update(Product dto);

    void delete(Long id);

}
