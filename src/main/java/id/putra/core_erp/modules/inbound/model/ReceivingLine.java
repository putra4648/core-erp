package id.putra.core_erp.modules.inbound.model;

import java.math.BigDecimal;
import java.sql.Date;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.warehouse.model.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ReceivingLine {
	private Date expiryDate;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;
	private String lotBatch;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(precision = 12, scale = 2, name = "ordered_qty")
	private BigDecimal qty;

	@Column(precision = 12, scale = 2)
	private BigDecimal qtyReceived;

	private Date receivedDate;

	@ManyToOne
	@JoinColumn(name = "receiving_id")
	private Receiving receiving;

	private String status;

	public Date getExpiryDate() {
		return expiryDate;
	}

	public Long getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public String getLotBatch() {
		return lotBatch;
	}

	public Product getProduct() {
		return product;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public BigDecimal getQtyReceived() {
		return qtyReceived;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public Receiving getReceiving() {
		return receiving;
	}

	public String getStatus() {
		return status;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setLotBatch(String lotBatch) {
		this.lotBatch = lotBatch;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public void setQtyReceived(BigDecimal qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public void setReceiving(Receiving receiving) {
		this.receiving = receiving;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
