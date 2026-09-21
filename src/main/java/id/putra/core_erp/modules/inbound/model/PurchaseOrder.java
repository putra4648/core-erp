package id.putra.core_erp.modules.inbound.model;

import java.sql.Date;
import java.util.List;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.shared.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String poNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Date orderDate;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderLine> purchaseOrderLines;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<Receiving> receivings;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<PurchaseOrderLine> getPurchaseOrderLines() {
        return purchaseOrderLines;
    }

    public void setPurchaseOrderLines(List<PurchaseOrderLine> purchaseOrderLines) {
        this.purchaseOrderLines = purchaseOrderLines;
    }

    public List<Receiving> getReceivings() {
        return receivings;
    }

    public void setReceivings(List<Receiving> receivings) {
        this.receivings = receivings;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
