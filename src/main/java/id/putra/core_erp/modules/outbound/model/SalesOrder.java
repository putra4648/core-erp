package id.putra.core_erp.modules.outbound.model;

import java.sql.Date;
import java.util.List;

import id.putra.core_erp.modules.customer.model.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class SalesOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String soNumber;
  private Date orderDate;
  private String status;

  @OneToMany(mappedBy = "salesOrder")
  private List<SalesOrderLine> salesOrderLines;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSoNumber() {
    return soNumber;
  }

  public void setSoNumber(String soNumber) {
    this.soNumber = soNumber;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<SalesOrderLine> getSalesOrderLines() {
    return salesOrderLines;
  }

  public void setSalesOrderLines(List<SalesOrderLine> salesOrderLines) {
    this.salesOrderLines = salesOrderLines;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}
