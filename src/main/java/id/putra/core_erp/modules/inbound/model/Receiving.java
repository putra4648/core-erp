package id.putra.core_erp.modules.inbound.model;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.OrderStatus;
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
public class Receiving {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;
	private Date receivedDate;
	@OneToMany(mappedBy = "receiving", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReceivingLine> receivingLines;

	private String receivingNumber = UUID.randomUUID().toString();
=======
public class Receiving extends BaseEntity {

	private String receivingNumber = UUID.randomUUID().toString();
	private Date receivedDate;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(mappedBy = "receiving", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReceivingLine> receivingLines;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	// @CreatedBy
	// private String receivedBy;

<<<<<<< HEAD
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public Long getId() {
		return id;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
=======
	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;

	public String getReceivingNumber() {
		return receivingNumber;
	}

	public void setReceivingNumber(String receivingNumber) {
		this.receivingNumber = receivingNumber;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

<<<<<<< HEAD
	public List<ReceivingLine> getReceivingLines() {
		return receivingLines;
	}

	public String getReceivingNumber() {
		return receivingNumber;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public void setReceivingLines(List<ReceivingLine> receivingLines) {
		this.receivingLines = receivingLines;
	}

	public void setReceivingNumber(String receivingNumber) {
		this.receivingNumber = receivingNumber;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
=======
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public List<ReceivingLine> getReceivingLines() {
		return receivingLines;
	}

	public void setReceivingLines(List<ReceivingLine> receivingLines) {
		this.receivingLines = receivingLines;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
