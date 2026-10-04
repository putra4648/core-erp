package id.putra.core_erp.modules.outbound.model;

import java.sql.Date;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Shipment extends BaseEntity{
  private String shipmentNumber;
  private Date shipDate;
  private String status;

  @ManyToOne
  @JoinColumn(name = "packing_id")
  private Packing packing;

  @ManyToOne
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;


  

  public String getShipmentNumber() {
    return shipmentNumber;
  }

  public void setShipmentNumber(String shipmentNumber) {
    this.shipmentNumber = shipmentNumber;
  }

  public Date getShipDate() {
    return shipDate;
  }

  public void setShipDate(Date shipDate) {
    this.shipDate = shipDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Packing getPacking() {
    return packing;
  }

  public void setPacking(Packing packing) {
    this.packing = packing;
  }

  public Carrier getCarrier() {
    return carrier;
  }

  public void setCarrier(Carrier carrier) {
    this.carrier = carrier;
  }
}
