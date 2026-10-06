package id.putra.core_erp.modules.procurement.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.annotations.Formula;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "requisition_detail")
public class RequisitionDetail extends BaseEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "item_number")
	private String itemNumber;

	@Column(name = "description")
	private String description;

	@Column(name = "note")
	private String note;

	// Only path
	@Column(name = "spec_files")
	private List<String> specFiles;

	@Column(name = "uom")
	private String uom;

	@Column(name = "qty")
	private BigInteger qty;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	@Column(name = "supplier")
	private String supplier;

	private BigDecimal subtotal;

	// @ManyToOne
	// @JoinColumn(name = "supplier_id", referencedColumnName = "acc_no")
	// private Supplier supplier;

	@Formula("qty * currency")
	@Column(name = "sub_total")
	private BigInteger subTotal;

	@ManyToOne
	@JoinColumn(name = "requisition_id", referencedColumnName = "id")
	private Requisition requisition;

	@ManyToOne
	@JoinColumn(name = "rfq_id", referencedColumnName = "id")
	private Quotation rfq;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<String> getSpecFiles() {
		return specFiles;
	}

	public void setSpecFiles(List<String> specFiles) {
		this.specFiles = specFiles;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public BigInteger getQty() {
		return qty;
	}

	public void setQty(BigInteger qty) {
		this.qty = qty;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public BigInteger getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigInteger subTotal) {
		this.subTotal = subTotal;
	}

	public Requisition getRequisition() {
		return requisition;
	}

	public void setRequisition(Requisition requisition) {
		this.requisition = requisition;
	}

	public Quotation getRfq() {
		return rfq;
	}

	public void setRfq(Quotation rfq) {
		this.rfq = rfq;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

}
