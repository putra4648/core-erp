package id.putra.core_erp.warehouse.module.warehouse.dto;

import java.util.List;

import id.putra.core_erp.shared.dto.BaseDto;

public class WarehouseDto extends BaseDto {
    private Long id;
    private String name;
    private Boolean isActive;
    private String location;
    private Double area;
    private Integer total;
    private List<ZoneDto> zones;
    private ContactPersonWarehouseDto contactPersonWarehouse;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ZoneDto> getZones() {
        return zones;
    }

    public void setZones(List<ZoneDto> zones) {
        this.zones = zones;
    }

    public ContactPersonWarehouseDto getContactPersonWarehouse() {
        return contactPersonWarehouse;
    }

    public void setContactPersonWarehouse(ContactPersonWarehouseDto contactPersonWarehouse) {
        this.contactPersonWarehouse = contactPersonWarehouse;
    }
}
