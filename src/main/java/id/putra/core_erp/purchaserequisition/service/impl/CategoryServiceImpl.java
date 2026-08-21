package id.putra.core_erp.purchaserequisition.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import id.putra.core_erp.purchaserequisition.dto.CategoryDto;
import id.putra.core_erp.shared.entity.Category;
import id.putra.core_erp.purchaserequisition.repository.CategoryRepo;
import id.putra.core_erp.purchaserequisition.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service("categoryService")
@Transactional(rollbackOn = { Exception.class })
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Override
    public void addCategory(CategoryDto dto) {
        List<Map<String, Object>> categories = categoryRepo.getAllCategories(dto.getName());

        if (categories.isEmpty()) {
            Category entity = new Category();
            entity.setName(dto.getName());
            if (dto.getParent_id() != null && !dto.getParent_id().isBlank() && !"null".equalsIgnoreCase(dto.getParent_id())) {
                entity.setParentId(Long.parseLong(dto.getParent_id()));
            } else {
                entity.setParentId(null);
            }
            categoryRepo.save(entity);
        } else {
            throw new IllegalArgumentException("Category with name '" + dto.getName() + "' already exists");
        }
    }

    @Override
    public List<CategoryDto> getCategory() {
        List<CategoryDto> categories = new ArrayList<>();

        categoryRepo.getAllCategories("").forEach(data -> {
            categories.add(entityToDto(data));
        });

        Map<Integer, CategoryDto> categoryMap = new HashMap<>();
        for (CategoryDto category : categories) {
            if (category.getId() != null && !category.getId().equalsIgnoreCase("null")) {
                categoryMap.put(Integer.parseInt(category.getId()), category);
            }
        }

        return buildHierarchy(categories, categoryMap);
    }

    private CategoryDto entityToDto(Category entity) {
        CategoryDto dto = new CategoryDto();

        dto.setId(String.valueOf(entity.getId()));
        dto.setName(entity.getName());
        dto.setParent_id(String.valueOf(entity.getParentId()));

        return dto;
    }

    private CategoryDto entityToDto(Map<String, Object> entity) {
        CategoryDto dto = new CategoryDto();

        dto.setId(String.valueOf(entity.get("id")));
        dto.setName((String) entity.get("name"));
        dto.setParent_id(String.valueOf(entity.get("parent_id")));

        return dto;
    }

    public List<CategoryDto> buildHierarchy(List<CategoryDto> categories, Map<Integer, CategoryDto> categoryMap) {
        List<CategoryDto> rootCategories = new ArrayList<>();

        for (CategoryDto category : categories) {
            if (category.getParent_id() == null || category.getParent_id().equalsIgnoreCase("null") || category.getParent_id().isBlank()) {
                // Root node
                rootCategories.add(category);
            } else {
                // Child node, find its parent and add it as a child
                CategoryDto parentCategory = categoryMap.get(Integer.parseInt(category.getParent_id()));
                if (parentCategory != null) {
                    parentCategory.getChildren().add(category);
                }
            }
        }
        return rootCategories;
    }

    @Override
    public void updateCategory(CategoryDto dto) {
        if (dto.getId() != null && !dto.getId().isBlank()) {
            long id = Long.parseLong(dto.getId());
            categoryRepo.findById(id).ifPresent(entity -> {
                entity.setName(dto.getName());
                if (dto.getParent_id() != null && !dto.getParent_id().isBlank() && !"null".equalsIgnoreCase(dto.getParent_id())) {
                    entity.setParentId(Long.parseLong(dto.getParent_id()));
                } else {
                    entity.setParentId(null);
                }
                categoryRepo.save(entity);
            });
        }
    }

    @Override
    public List<CategoryDto> getCategoryDropdown() {
        List<CategoryDto> dtos = new ArrayList<>();

        categoryRepo.findAll().forEach(data -> {
            dtos.add(entityToDto(data));
        });
        return dtos;
    }

}


