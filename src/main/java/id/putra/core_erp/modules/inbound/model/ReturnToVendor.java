package id.putra.core_erp.modules.inbound.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ReturnToVendor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String reason;
  @Column(precision = 12, scale = 2)
  private BigDecimal qty;

  @ManyToOne
  @JoinColumn(name = "receiving_line_id")
  private ReceivingLine receivingLine;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public ReceivingLine getReceivingLine() {
    return receivingLine;
  }

  public void setReceivingLine(ReceivingLine receivingLine) {
    this.receivingLine = receivingLine;
  }
}
