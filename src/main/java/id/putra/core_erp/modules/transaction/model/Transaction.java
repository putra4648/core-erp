package id.putra.core_erp.modules.transaction.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {
<<<<<<< HEAD
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Department customer;

	private String destLocId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
	private List<TransactionItem> items;
	private String notes;
	private Long quantity;
	private String refNumber;
	private String refType;
	private String sourceLocId;
	private String status;
	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;
	private BigDecimal totalAmount;
	private ZonedDateTime txnDate;
	private String txnNumber;

	private String txnType;

	public Department getCustomer() {
		return customer;
	}

	public String getDestLocId() {
		return destLocId;
	}

	public Long getId() {
		return id;
	}

	public List<TransactionItem> getItems() {
		return items;
	}

	public String getNotes() {
		return notes;
	}

	public Long getQuantity() {
		return quantity;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public String getRefType() {
		return refType;
	}

	public String getSourceLocId() {
		return sourceLocId;
	}

	public String getStatus() {
		return status;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public ZonedDateTime getTxnDate() {
		return txnDate;
	}

	public String getTxnNumber() {
		return txnNumber;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setCustomer(Department customer) {
		this.customer = customer;
	}

	public void setDestLocId(String destLocId) {
		this.destLocId = destLocId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setItems(List<TransactionItem> items) {
		this.items = items;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public void setSourceLocId(String sourceLocId) {
		this.sourceLocId = sourceLocId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setTxnDate(ZonedDateTime txnDate) {
		this.txnDate = txnDate;
	}

	public void setTxnNumber(String txnNumber) {
		this.txnNumber = txnNumber;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
=======

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Department customer;

	private String txnNumber;
	private String txnType;
	private ZonedDateTime txnDate;
	private String sourceLocId;
	private String destLocId;
	private String refNumber;
	private String refType;
	private String status;
	private String notes;
	private BigDecimal totalAmount;
	private Long quantity;

	@OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
	private List<TransactionItem> items;

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Department getCustomer() {
		return customer;
	}

	public void setCustomer(Department customer) {
		this.customer = customer;
	}

	public String getTxnNumber() {
		return txnNumber;
	}

	public void setTxnNumber(String txnNumber) {
		this.txnNumber = txnNumber;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public ZonedDateTime getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(ZonedDateTime txnDate) {
		this.txnDate = txnDate;
	}

	public String getSourceLocId() {
		return sourceLocId;
	}

	public void setSourceLocId(String sourceLocId) {
		this.sourceLocId = sourceLocId;
	}

	public String getDestLocId() {
		return destLocId;
	}

	public void setDestLocId(String destLocId) {
		this.destLocId = destLocId;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public List<TransactionItem> getItems() {
		return items;
	}

	public void setItems(List<TransactionItem> items) {
		this.items = items;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
