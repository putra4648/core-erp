package id.putra.core_erp.warehouse.module.inventory.service.core;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.inventory.dto.CategoryDto;

public interface CategoryCoreService {

    Page<CategoryDto> getCategories(CategoryDto dto, Pageable pageable);

    CategoryDto getCategory(CategoryDto dto);

    void save(List<CategoryDto> dtos);

    void update(List<CategoryDto> dtos);

    void delete(List<CategoryDto> dtos);

}

