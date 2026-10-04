package id.putra.core_erp.modules.outbound.model;

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
public class PickingTask  extends BaseEntity{
  

    @ManyToOne
    @JoinColumn(name = "sales_order_line_id")
    private SalesOrderLine salesOrderLine;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SalesOrderLine getSalesOrderLine() {
        return salesOrderLine;
    }

    public void setSalesOrderLine(SalesOrderLine salesOrderLine) {
        this.salesOrderLine = salesOrderLine;
    }

}
