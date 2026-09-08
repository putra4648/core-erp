package id.putra.core_erp.warehouse.module.inventory.service.adapter.query.impl;

import id.putra.core_erp.shared.entity.Category;
import id.putra.core_erp.warehouse.module.inventory.model.repository.CategoryRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.CategoryQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryQueryAdapterImpl implements CategoryQueryAdapter {

    @Inject
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Category category) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
