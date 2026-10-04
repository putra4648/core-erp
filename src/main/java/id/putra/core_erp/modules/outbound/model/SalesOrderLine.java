package id.putra.core_erp.modules.outbound.model;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class SalesOrderLine extends BaseEntity{
  

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "sales_order_id")
  private SalesOrder salesOrder;

  

  

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public SalesOrder getSalesOrder() {
    return salesOrder;
  }

  public void setSalesOrder(SalesOrder salesOrder) {
    this.salesOrder = salesOrder;
  }
}
