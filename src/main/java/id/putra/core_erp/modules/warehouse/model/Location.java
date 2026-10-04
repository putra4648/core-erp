package id.putra.core_erp.modules.warehouse.model;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "warehouse_locations")
@Entity
public class Location extends BaseEntity {
    

    private String name;

    private String type;

    private String binNumber;

    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id")
    private Rack rack;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<ReceivingLine> receivingLines;

  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBinNumber() {
        return binNumber;
    }

    public void setBinNumber(String binNumber) {
        this.binNumber = binNumber;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public List<ReceivingLine> getReceivingLines() {
        return receivingLines;
    }

    public void setReceivingLines(List<ReceivingLine> receivingLines) {
        this.receivingLines = receivingLines;
    }

}
