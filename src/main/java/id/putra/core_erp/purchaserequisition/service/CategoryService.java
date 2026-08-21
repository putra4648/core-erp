package id.putra.core_erp.purchaserequisition.service;

import java.util.List;
import id.putra.core_erp.purchaserequisition.dto.CategoryDto;

public interface CategoryService {
    void addCategory(CategoryDto dto);

    List<CategoryDto> getCategory();

    void updateCategory(CategoryDto dto);

    List<CategoryDto> getCategoryDropdown();
}

