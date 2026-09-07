package id.putra.core_erp.warehouse.module.supplier.dto;

import java.util.List;

import id.putra.core_erp.shared.dto.BaseDto;

public class SupplierDto extends BaseDto {
    private Long id;
    private String name;
    private String code;
    private Boolean isActive;
    private List<ProductSupplierDto> products;
    private List<ContactPersonSupplierDto> contactPersonSuppliers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<ProductSupplierDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSupplierDto> products) {
        this.products = products;
    }

    public List<ContactPersonSupplierDto> getContactPersonSuppliers() {
        return contactPersonSuppliers;
    }

    public void setContactPersonSuppliers(List<ContactPersonSupplierDto> contactPersonSuppliers) {
        this.contactPersonSuppliers = contactPersonSuppliers;
    }

}
