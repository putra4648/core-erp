package id.putra.core_erp.modules.procurement.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.annotations.Formula;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "requisition_detail")
public class RequisitionDetail extends BaseEntity {
	@Column(name = "description")
	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "item_number")
	private String itemNumber;

	@Column(name = "name")
	private String name;

	@Column(name = "note")
	private String note;

	@Column(name = "qty")
	private BigInteger qty;

	@ManyToOne
	@JoinColumn(name = "requisition_id", referencedColumnName = "id")
	private Requisition requisition;

	@ManyToOne
	@JoinColumn(name = "rfq_id", referencedColumnName = "id")
	private Quotation rfq;

	// Only path
	@Column(name = "spec_files")
	private List<String> specFiles;

	private BigDecimal subtotal;

	@Formula("qty * currency")
	@Column(name = "sub_total")
	private BigInteger subTotal;

	// @ManyToOne
	// @JoinColumn(name = "supplier_id", referencedColumnName = "acc_no")
	// private Supplier supplier;

	@Column(name = "supplier")
	private String supplier;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	@Column(name = "uom")
	private String uom;

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}

	public BigInteger getQty() {
		return qty;
	}

	public Requisition getRequisition() {
		return requisition;
	}

	public Quotation getRfq() {
		return rfq;
	}

	public List<String> getSpecFiles() {
		return specFiles;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public BigInteger getSubTotal() {
		return subTotal;
	}

	public String getSupplier() {
		return supplier;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public String getUom() {
		return uom;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setQty(BigInteger qty) {
		this.qty = qty;
	}

	public void setRequisition(Requisition requisition) {
		this.requisition = requisition;
	}

	public void setRfq(Quotation rfq) {
		this.rfq = rfq;
	}

	public void setSpecFiles(List<String> specFiles) {
		this.specFiles = specFiles;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public void setSubTotal(BigInteger subTotal) {
		this.subTotal = subTotal;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

}
