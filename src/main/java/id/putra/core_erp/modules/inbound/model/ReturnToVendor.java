package id.putra.core_erp.modules.inbound.model;

import java.math.BigDecimal;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ReturnToVendor extends BaseEntity {
	public String reason;
	@Column(precision = 12, scale = 2)
	public BigDecimal qty;

	@ManyToOne
	@JoinColumn(name = "receiving_line_id")
	public ReceivingLine receivingLine;

	
}
