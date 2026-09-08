package id.putra.core_erp.warehouse.module.inventory.service.core;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Category;
import io.quarkus.panache.common.Page;

public interface CategoryCoreService {

    Category getCategory(Category category);

    void save(List<Category> categories);

    void update(List<Category> categories);

    void delete(List<Category> dtos);

    DataProvider<Category, Void> getCategories(String value, Page ofSize);

}
