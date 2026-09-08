package id.putra.core_erp.warehouse.module.inventory.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.shared.entity.Category;
import id.putra.core_erp.warehouse.module.inventory.model.repository.CategoryRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.CategoryCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryCommandAdapterImpl implements CategoryCommandAdapter {

    @Inject
    private CategoryRepository categoryRepository;

    @Override
    public void save(List<Category> categories) {

    }

    @Override
    public void update(List<Category> categories) {
    }

    @Override
    public void delete(List<Category> categories) {
    }

}
