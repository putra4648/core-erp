package id.putra.core_erp.modules.inbound.model;

import java.sql.Date;
import java.util.List;
import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.procurement.model.PurchaseOrder;
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

	public Date receivedDate;

	@Enumerated(EnumType.STRING)
	public OrderStatus status;

	@OneToMany(mappedBy = "receiving", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ReceivingLine> receivingLines;

	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	public PurchaseOrder purchaseOrder;

}
