package id.putra.core_erp.modules.transaction.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.customer.model.Customer;
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

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	public Supplier supplier;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	public Customer customer;

	public String txnNumber;
	public String txnType;
	public ZonedDateTime txnDate;
	public String sourceLocId;
	public String destLocId;
	public String refNumber;
	public String refType;
	public String status;
	public String notes;
	public BigDecimal totalAmount;
	public Integer quantity;

	@OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
	public List<TransactionItem> items;

}
