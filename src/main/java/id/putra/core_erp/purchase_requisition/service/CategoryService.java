package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.shared.entity.Category;

public interface CategoryService {
    void addCategory(Category dto);

    List<Category> getCategory();

    void updateCategory(Category dto);

    List<Category> getCategoryDropdown();
}
