package id.putra.core_erp.modules.inbound.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class QualityInspection extends BaseEntity {

	private String result;
	private String remarks;

	@ManyToOne
	@JoinColumn(name = "receiving_line_id")
	private ReceivingLine receivingLine;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public ReceivingLine getReceivingLine() {
		return receivingLine;
	}

	public void setReceivingLine(ReceivingLine receivingLine) {
		this.receivingLine = receivingLine;
	}

}
