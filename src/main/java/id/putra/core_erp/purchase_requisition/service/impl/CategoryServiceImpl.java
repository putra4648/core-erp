package id.putra.core_erp.purchase_requisition.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.putra.core_erp.purchase_requisition.repository.CategoryRepo;
import id.putra.core_erp.purchase_requisition.service.CategoryService;
import id.putra.core_erp.shared.entity.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(rollbackOn = { Exception.class })
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public void addCategory(Category dto) {
        List<Category> categories = categoryRepo.findAll().list();

        if (categories.isEmpty()) {
            Category entity = new Category();
            entity.setName(dto.getName());
            if (dto.getParentId() != null) {
                entity.setParentId(dto.getParentId());
            } else {
                entity.setParentId(null);
            }
            categoryRepo.persist(entity);
        } else {
            throw new IllegalArgumentException("Category with name '" + dto.getName() + "' already exists");
        }
    }

    @Override
    public List<Category> getCategory() {
        List<Category> categories = new ArrayList<>();

        categoryRepo.findAll().list().forEach(data -> {
            categories.add(entityToDto(data));
        });

        Map<Long, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            if (category.getId() != null) {
                categoryMap.put(category.getId(), category);
            }
        }

        return buildHierarchy(categories, categoryMap);
    }

    private Category entityToDto(Category entity) {
        Category dto = new Category();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setParentId(entity.getParentId());

        return dto;
    }

    public List<Category> buildHierarchy(List<Category> categories, Map<Long, Category> categoryMap) {
        List<Category> rootCategories = new ArrayList<>();

        for (Category category : categories) {
            if (category.getParentId() == null) {
                // Root node
                rootCategories.add(category);
            } else {
                // Child node, find its parent and add it as a child
                Category parentCategory = categoryMap.get(category.getParentId());
                // if (parentCategory != null) {
                // parentCategory.getChildren().add(category);
                // }
            }
        }
        return rootCategories;
    }

    @Override
    public void updateCategory(Category dto) {
        if (dto.getId() != null) {
            categoryRepo.findByIdOptional(dto.getId()).ifPresent(entity -> {
                entity.setName(dto.getName());
                if (dto.getParentId() != null) {
                    entity.setParentId(dto.getParentId());
                } else {
                    entity.setParentId(null);
                }
                categoryRepo.persist(entity);
            });
        }
    }

    @Override
    public List<Category> getCategoryDropdown() {
        List<Category> dtos = new ArrayList<>();

        categoryRepo.findAll().list().forEach(data -> {
            dtos.add(entityToDto(data));
        });
        return dtos;
    }

}
