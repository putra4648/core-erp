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
<<<<<<< HEAD
public class ReturnToVendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(precision = 12, scale = 2)
	private BigDecimal qty;
	private String reason;
=======
public class ReturnToVendor extends BaseEntity {
	private String reason;
	@Column(precision = 12, scale = 2)
	private BigDecimal qty;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@ManyToOne
	@JoinColumn(name = "receiving_line_id")
	private ReceivingLine receivingLine;

<<<<<<< HEAD
	public Long getId() {
		return id;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public String getReason() {
		return reason;
	}

	public ReceivingLine getReceivingLine() {
		return receivingLine;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

=======
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public ReceivingLine getReceivingLine() {
		return receivingLine;
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	public void setReceivingLine(ReceivingLine receivingLine) {
		this.receivingLine = receivingLine;
	}
}
