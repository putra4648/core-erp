package id.putra.core_erp.modules.inbound.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class QualityInspection extends BaseEntity {

	public String result;
	public String remarks;

	@ManyToOne
	@JoinColumn(name = "receiving_line_id")
	public ReceivingLine receivingLine;

	

}
