package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.purchase_requisition.dto.CategoryDto;

public interface CategoryService {
    void addCategory(CategoryDto dto);

    List<CategoryDto> getCategory();

    void updateCategory(CategoryDto dto);

    List<CategoryDto> getCategoryDropdown();
}
