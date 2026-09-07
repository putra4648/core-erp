package id.putra.core_erp.warehouse.module.warehouse.dto;

import java.util.List;

import id.putra.core_erp.shared.dto.BaseDto;

public class ZoneDto extends BaseDto {
    private Long id;
    private String name;
    private String code;
    private Boolean isActive;
    private String type;
    /** parent warehouse id to avoid circular nested DTOs */
    private String warehouseId;
    private List<RackDto> racks;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<RackDto> getRacks() {
        return racks;
    }

    public void setRacks(List<RackDto> racks) {
        this.racks = racks;
    }

}
