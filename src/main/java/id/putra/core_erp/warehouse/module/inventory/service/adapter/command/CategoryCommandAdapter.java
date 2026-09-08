package id.putra.core_erp.warehouse.module.inventory.service.adapter.command;

import java.util.List;

import id.putra.core_erp.shared.entity.Category;

public interface CategoryCommandAdapter {

    void save(List<Category> categories);

    void update(List<Category> categories);

    void delete(List<Category> categories);

}
