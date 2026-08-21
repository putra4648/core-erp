package id.putra.core_erp.warehouse.module.inventory.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.inventory.dto.CategoryDto;

public interface CategoryQueryAdapter {

    Page<CategoryDto> getCategories(CategoryDto dto, Pageable pageable);

    CategoryDto getCategoryById(CategoryDto dto);

}

