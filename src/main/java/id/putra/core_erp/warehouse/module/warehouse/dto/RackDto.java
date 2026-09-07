package id.putra.core_erp.warehouse.module.warehouse.dto;

import java.util.List;

import id.putra.core_erp.shared.dto.BaseDto;

public class RackDto extends BaseDto {
    private Long id;
    private String name;
    private Long rows;
    private Long cols;
    private Boolean isActive;
    private List<LocationDto> locations;
    private ZoneDto zone;

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

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public Long getCols() {
        return cols;
    }

    public void setCols(Long cols) {
        this.cols = cols;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<LocationDto> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDto> locations) {
        this.locations = locations;
    }

    public ZoneDto getZone() {
        return zone;
    }

    public void setZone(ZoneDto zone) {
        this.zone = zone;
    }

}
