package id.putra.core_erp.modules.inbound.model;

import java.sql.Date;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class PurchaseOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date orderDate;

	private String poNumber;

	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PurchaseOrderLine> purchaseOrderLines;

	@OneToMany(mappedBy = "purchaseOrder")
	private List<Receiving> receivings;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	public Long getId() {
		return id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public List<PurchaseOrderLine> getPurchaseOrderLines() {
		return purchaseOrderLines;
	}

	public List<Receiving> getReceivings() {
		return receivings;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public void setPurchaseOrderLines(List<PurchaseOrderLine> purchaseOrderLines) {
		this.purchaseOrderLines = purchaseOrderLines;
	}

	public void setReceivings(List<Receiving> receivings) {
		this.receivings = receivings;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

=======
public class PurchaseOrder extends BaseEntity {

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

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
}
