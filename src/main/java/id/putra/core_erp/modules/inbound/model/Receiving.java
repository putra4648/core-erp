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
public class Receiving extends BaseEntity {

	private String receivingNumber = UUID.randomUUID().toString();
	private Date receivedDate;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(mappedBy = "receiving", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReceivingLine> receivingLines;

	// @CreatedBy
	// private String receivedBy;

	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;

	public String getReceivingNumber() {
		return receivingNumber;
	}

	public void setReceivingNumber(String receivingNumber) {
		this.receivingNumber = receivingNumber;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

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
	}
}
