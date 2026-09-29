package id.putra.core_erp.modules.inbound.model;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import id.putra.core_erp.enums.OrderStatus;
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

	// @CreatedBy
	// private String receivedBy;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public Long getId() {
		return id;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

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
	}
}
