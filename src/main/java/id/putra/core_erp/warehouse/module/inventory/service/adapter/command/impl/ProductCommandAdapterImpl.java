package id.putra.core_erp.warehouse.module.inventory.service.adapter.command.impl;

import id.putra.core_erp.shared.dto.ProductDto;
import id.putra.core_erp.warehouse.module.inventory.model.repository.ProductRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.ProductCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductCommandAdapterImpl implements ProductCommandAdapter {

    @Inject
    private ProductRepository productRepository;

    @Override
    public void save(ProductDto dto) {

    }

    @Override
    public void update(ProductDto dto) {
    }

    @Override
    public void delete(Long id) {

    }

}
