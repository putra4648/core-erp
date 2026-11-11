package id.putra.core_erp.modules.inbound.service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import id.putra.core_erp.modules.inbound.model.Putaway;
import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.modules.inbound.repository.PutawayRepository;
import id.putra.core_erp.modules.inbound.repository.ReceivingLineRepository;
import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.inventory.repository.InventoryItemRepository;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.repository.WarehouseRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PutawayService implements BaseService<Putaway> {
	private final PutawayRepository putawayRepository;
	private final ReceivingLineRepository receivingLineRepository;
	private final InventoryItemRepository inventoryItemRepository;
	private final WarehouseRepository warehouseRepository;

	public PutawayService(PutawayRepository putawayRepository, ReceivingLineRepository receivingLineRepository,
			InventoryItemRepository inventoryItemRepository, WarehouseRepository warehouseRepository) {
		this.putawayRepository = putawayRepository;
		this.receivingLineRepository = receivingLineRepository;
		this.inventoryItemRepository = inventoryItemRepository;
		this.warehouseRepository = warehouseRepository;
	}

	@Override
	public void save(Putaway entity) {
		putawayRepository.persist(entity);
	}

	@Override
	public void update(Putaway dto) {
		putawayRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		putawayRepository.softDeleteById(id);
	}

	@Override
	public Putaway findById(Long id) {
		return putawayRepository.findById(id);
	}

	@Override
	public List<Putaway> findAll(Putaway putAway, Page p) {
		return putawayRepository.findAll().list();
	}

	/**
	 * Executes a putaway for a single receiving line, moving the received goods
	 * into warehouse storage and incrementing the {@link InventoryItem} balance
	 * (P2P step: "Putaway to Storage").
	 * <p>
	 * <ul>
	 * <li>Creates a {@link Putaway} record linked to the receiving line, product
	 * and target warehouse.</li>
	 * <li>Finds an existing {@link InventoryItem} for the same product + lot (or
	 * creates a new one) and increments its quantity by the received amount.</li>
	 * <li>Marks the receiving line status as {@code PUTAWAY}.</li>
	 * </ul>
	 *
	 * @param receivingLineId the dock receiving line to put away
	 * @param warehouseId     the target warehouse for storage
	 */
	public void executePutaway(Long receivingLineId, Long warehouseId) {
		ReceivingLine line = receivingLineRepository.findById(receivingLineId);
		if (line == null) {
			throw new RuntimeException("Receiving line not found");
		}
		if (line.qtyReceived == null || line.qtyReceived.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("Cannot put away: no quantity received for this line");
		}
		Warehouse warehouse = warehouseRepository.findById(warehouseId);
		if (warehouse == null) {
			throw new RuntimeException("Warehouse not found");
		}

		// create the putaway record
		Putaway putaway = new Putaway();
		putaway.receivingLine = line;
		putaway.product = line.product;
		putaway.warehouse = warehouse;
		putaway.qty = line.qtyReceived;
		putaway.sku = line.product != null ? line.product.sku : null;
		putaway.lotNumber = line.lotBatch;
		putaway.inboundId = line.receiving != null ? line.receiving.id : null;
		putaway.putaway = warehouse.name;
		putaway.status = "DONE";
		putawayRepository.persist(putaway);

		// find or create the inventory item balance for this product + lot
		InventoryItem item = null;
		if (line.product != null) {
			List<InventoryItem> candidates = inventoryItemRepository.find("product.id", line.product.id).list();
			for (InventoryItem c : candidates) {
				if (Objects.equals(c.lotNumber, line.lotBatch)) {
					item = c;
					break;
				}
			}
		}
		if (item == null) {
			item = new InventoryItem();
			item.product = line.product;
			item.lotNumber = line.lotBatch;
			item.name = line.product != null ? line.product.name : null;
			item.quantity = 0L;
			item.reservedQty = 0L;
			item.status = "ACTIVE";
			if (line.expiryDate != null) {
				item.expiredDate = line.expiryDate.atZone(ZoneId.systemDefault());
			}
			inventoryItemRepository.persist(item);
		}
		long addQty = line.qtyReceived.longValue();
		item.quantity = (item.quantity != null ? item.quantity : 0L) + addQty;
		inventoryItemRepository.getEntityManager().merge(item);

		// mark the receiving line as put away
		line.status = "PUTAWAY";
		receivingLineRepository.getEntityManager().merge(line);
	}

}

