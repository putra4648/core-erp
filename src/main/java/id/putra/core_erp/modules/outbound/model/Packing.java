package id.putra.core_erp.modules.outbound.model;

import java.math.BigDecimal;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Packing extends BaseEntity {
 
  private String packageNumber;
  @Column(precision = 12, scale = 2)
  private BigDecimal weight;
  private String dimension;

  @OneToMany(mappedBy = "packing")
  private List<Shipment> shipments;

  

  public String getPackageNumber() {
    return packageNumber;
  }

  public void setPackageNumber(String packageNumber) {
    this.packageNumber = packageNumber;
  }

  public BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(BigDecimal weight) {
    this.weight = weight;
  }

  public String getDimension() {
    return dimension;
  }

  public void setDimension(String dimension) {
    this.dimension = dimension;
  }

  public List<Shipment> getShipments() {
    return shipments;
  }

  public void setShipments(List<Shipment> shipments) {
    this.shipments = shipments;
  }
}
