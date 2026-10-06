package id.putra.core_erp.modules.inbound.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class QualityInspection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "receiving_line_id")
	private ReceivingLine receivingLine;
	private String remarks;

	private String result;

	public Long getId() {
		return id;
	}

	public ReceivingLine getReceivingLine() {
		return receivingLine;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getResult() {
		return result;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setReceivingLine(ReceivingLine receivingLine) {
		this.receivingLine = receivingLine;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setResult(String result) {
		this.result = result;
=======
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

}
