package id.putra.core_erp.modules.inbound.model;

import java.math.BigDecimal;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Putaway extends BaseEntity {

	public String sku;
	public String lotNumber;
	public Long inboundId;
	public String outboundId;
	public String pickId;
	public String putaway;

	/**
	 * The receiving line whose goods are being put away into storage. Links the
	 * Putaway step back to the dock receipt for full P2P traceability
	 * (PO -&gt; Receiving -&gt; Putaway).
	 */
	@ManyToOne
	@JoinColumn(name = "receiving_line_id")
	public ReceivingLine receivingLine;

	@ManyToOne
	@JoinColumn(name = "product_id")
	public Product product;

	/**
	 * Target warehouse where the goods are stored after putaway.
	 */
	@ManyToOne
	@JoinColumn(name = "warehouse_id")
	public Warehouse warehouse;

	@Column(precision = 12, scale = 2)
	public BigDecimal qty;

	/**
	 * Putaway status (e.g. {@code PENDING}, {@code DONE}).
	 */
	public String status;

}

