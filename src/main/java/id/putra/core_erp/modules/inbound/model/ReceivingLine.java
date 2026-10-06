package id.putra.core_erp.modules.inbound.model;

import java.math.BigDecimal;
import java.sql.Date;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
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
=======
public class ReceivingLine extends BaseEntity {

	private Date receivedDate;
	private String status;
	private String lotBatch;
	private Date expiryDate;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@Column(precision = 12, scale = 2)
	private BigDecimal qtyReceived;

<<<<<<< HEAD
	private Date receivedDate;
=======
	@Column(precision = 12, scale = 2, name = "ordered_qty")
	private BigDecimal qty;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@ManyToOne
	@JoinColumn(name = "receiving_id")
	private Receiving receiving;

<<<<<<< HEAD
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
=======
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLotBatch() {
		return lotBatch;
	}

	public void setLotBatch(String lotBatch) {
		this.lotBatch = lotBatch;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigDecimal getQtyReceived() {
		return qtyReceived;
	}

	public void setQtyReceived(BigDecimal qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Receiving getReceiving() {
		return receiving;
	}

	public void setReceiving(Receiving receiving) {
		this.receiving = receiving;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
