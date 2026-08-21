package id.putra.core_erp.warehouse.module.inventory.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.dto.CategoryDto;

public interface CategoryCommandAdapter {

    void save(List<CategoryDto> dtos);

    void update(List<CategoryDto> dtos);

    void delete(List<CategoryDto> dtos);

}

