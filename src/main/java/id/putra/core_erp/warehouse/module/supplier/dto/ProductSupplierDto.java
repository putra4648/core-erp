package id.putra.core_erp.warehouse.module.supplier.dto;

import id.putra.core_erp.shared.dto.BaseDto;
import id.putra.core_erp.shared.dto.ProductDto;

public class ProductSupplierDto extends BaseDto {
    private ProductSupplierIdDto id;
    private ProductDto product;

    public ProductSupplierIdDto getId() {
        return id;
    }

    public void setId(ProductSupplierIdDto id) {
        this.id = id;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }
}
