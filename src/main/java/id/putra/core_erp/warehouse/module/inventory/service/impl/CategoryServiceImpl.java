package id.putra.core_erp.warehouse.module.inventory.service.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Category;
import id.putra.core_erp.warehouse.module.inventory.model.repository.CategoryRepository;
import id.putra.core_erp.warehouse.module.inventory.service.CategoryCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CategoryServiceImpl implements CategoryCoreService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategory(Category category) {
        return categoryRepository.findById(category.getId());
    }

    @Override
    public void save(List<Category> categories) {
        categoryRepository.persist(categories);
    }

    @Override
    public void update(List<Category> categories) {
        categories.forEach(category -> categoryRepository.getEntityManager().merge(category));
    }

    @Override
    public void delete(List<Category> categories) {
        categories.forEach(category -> categoryRepository.deleteById(category.getId()));
    }

    @Override
    public DataProvider<Category, Void> getCategories(String value, Page page) {
        String pattern = "%" + (value == null ? "" : value.trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> categoryRepository.find("lower(name) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(categoryRepository.count("lower(name) like ?1", pattern)));
    }

}
