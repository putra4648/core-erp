package id.putra.core_erp.warehouse.module.inventory.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.dto.CategoryDto;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.CategoryCommandAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.CategoryQueryAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.core.CategoryCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryCoreService {
    @Inject
    private CategoryQueryAdapter categoryQueryAdapter;

    @Inject
    private CategoryCommandAdapter categoryCommandAdapter;

    @Override
    public CategoryDto getCategory(CategoryDto dto) {
        return categoryQueryAdapter.getCategoryById(dto);
    }

    @Override
    public void save(List<CategoryDto> dtos) {
        categoryCommandAdapterpersist(dtos);
    }

    @Override
    public void update(List<CategoryDto> dtos) {
        categoryCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<CategoryDto> dtos) {
        categoryCommandAdapter.delete(dtos);
    }

}
