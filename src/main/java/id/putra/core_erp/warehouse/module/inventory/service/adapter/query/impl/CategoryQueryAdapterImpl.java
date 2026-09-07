package id.putra.core_erp.warehouse.module.inventory.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.inventory.dto.CategoryDto;
import id.putra.core_erp.warehouse.module.inventory.model.repository.CategoryRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.CategoryQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryQueryAdapterImpl implements CategoryQueryAdapter {

    @Inject
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto getCategoryById(CategoryDto dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
