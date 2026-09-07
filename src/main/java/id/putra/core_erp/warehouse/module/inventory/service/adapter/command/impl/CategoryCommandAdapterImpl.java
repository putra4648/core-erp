package id.putra.core_erp.warehouse.module.inventory.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.dto.CategoryDto;
import id.putra.core_erp.warehouse.module.inventory.model.repository.CategoryRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.CategoryCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryCommandAdapterImpl implements CategoryCommandAdapter {

    @Inject
    private CategoryRepository categoryRepository;

    @Override
    public void save(List<CategoryDto> dtos) {

    }

    @Override
    public void update(List<CategoryDto> dtos) {
    }

    @Override
    public void delete(List<CategoryDto> dtos) {
    }

}
