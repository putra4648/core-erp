package id.putra.core_erp.modules.inventory.model;

import java.time.ZonedDateTime;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "inventory_item")
@Entity
public class InventoryItem extends BaseEntity {
	private String name;
	private Long reservedQty;
	private String lotNumber;
	private ZonedDateTime expiredDate;
	private String status;
	private String serialNumber;
	private Long quantity;

<<<<<<< HEAD
	private ZonedDateTime expiredDate;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String lotNumber;
	private String name;
	@ManyToOne
	@JoinColumn(name = "product_sku")
	private Product product;
	private Long quantity;
	private Long reservedQty;
	private String serialNumber;

	private String status;

	public ZonedDateTime getExpiredDate() {
		return expiredDate;
	}

	public Long getId() {
		return id;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public String getName() {
		return name;
	}

	public Product getProduct() {
		return product;
	}

	public Long getQuantity() {
		return quantity;
	}

	public Long getReservedQty() {
		return reservedQty;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setExpiredDate(ZonedDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setReservedQty(Long reservedQty) {
		this.reservedQty = reservedQty;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setStatus(String status) {
		this.status = status;
=======
	@ManyToOne
	@JoinColumn(name = "product_sku")
	private Product product;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getReservedQty() {
		return reservedQty;
	}

	public void setReservedQty(Long reservedQty) {
		this.reservedQty = reservedQty;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public ZonedDateTime getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(ZonedDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

}
