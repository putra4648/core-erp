package id.putra.core_erp.warehouse.module.warehouse.dto;

import java.util.List;

import id.putra.core_erp.shared.dto.BaseDto;

public class ContactPersonWarehouseDto extends BaseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private List<WarehouseDto> warehouses;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<WarehouseDto> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<WarehouseDto> warehouses) {
        this.warehouses = warehouses;
    }

}
