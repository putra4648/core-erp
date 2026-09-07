package id.putra.core_erp.warehouse.module.supplier.dto;

public class ProductSupplierIdDto {
    private Long productId;
    private String supplierId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

}
