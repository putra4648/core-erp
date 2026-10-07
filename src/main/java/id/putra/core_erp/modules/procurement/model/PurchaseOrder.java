package id.putra.core_erp.modules.procurement.model;

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
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class PurchaseOrder extends BaseEntity {

	@NotBlank(message = "PO Number is required")
	public String poNumber;

	@Enumerated(EnumType.STRING)
	public OrderStatus status;

	public Date orderDate;

	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PurchaseOrderLine> purchaseOrderLines;

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	public Supplier supplier;

}
