package id.putra.core_erp.modules.procurement.model;

import java.math.BigDecimal;
import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class RequisitionItem extends BaseEntity {

	@Column(name = "name")
	@NotNull(message = "Item Name is required")
	public String name;

	@Column(name = "quantity")
	@NotNull(message = "Quantity is required")
	public Integer quantity;

	@Column(name = "unit_price")
	@NotNull(message = "Price is required")
	public BigDecimal unitPrice;

	@ManyToOne
	@JoinColumn(name = "requisition_id", referencedColumnName = "id")
	public Requisition requisition;

}
