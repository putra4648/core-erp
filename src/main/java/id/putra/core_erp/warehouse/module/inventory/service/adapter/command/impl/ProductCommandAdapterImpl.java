package id.putra.core_erp.warehouse.module.inventory.service.adapter.command.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.inventory.model.repository.ProductRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.ProductCommandAdapter;
import id.putra.core_erp.warehouse.shared.base.dto.ProductDto;
import id.putra.core_erp.warehouse.shared.converter.ProductConverter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ProductCommandAdapterImpl implements ProductCommandAdapter {

    private final ProductRepository productRepository;

    @Override
    public ProductDto save(ProductDto dto) {
        var entity = ProductConverter.toEntity(dto);
        return ProductConverter.toDto(productRepository.save(entity));
    }

    @Override
    public ProductDto update(ProductDto dto) {
        var entity = ProductConverter.toEntity(dto);
        return ProductConverter.toDto(productRepository.save(entity));
    }

    @Override
    public Boolean delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

