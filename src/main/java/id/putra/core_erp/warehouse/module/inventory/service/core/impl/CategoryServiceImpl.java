package id.putra.core_erp.warehouse.module.inventory.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Category;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.CategoryCommandAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.CategoryQueryAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.core.CategoryCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryCoreService {
    @Inject
    private CategoryQueryAdapter categoryQueryAdapter;

    @Inject
    private CategoryCommandAdapter categoryCommandAdapter;

    @Override
    public Category getCategory(Category category) {
        return categoryQueryAdapter.getCategoryById(category);
    }

    @Override
    public void save(List<Category> categories) {
        categoryCommandAdapter.save(categories);
    }

    @Override
    public void update(List<Category> categories) {
        categoryCommandAdapter.update(categories);
    }

    @Override
    public void delete(List<Category> categories) {
        categoryCommandAdapter.delete(categories);
    }

    @Override
    public DataProvider<Category, Void> getCategories(String value, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategories'");
    }

}
