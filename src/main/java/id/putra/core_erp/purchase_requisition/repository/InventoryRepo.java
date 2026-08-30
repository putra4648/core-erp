package id.putra.core_erp.purchase_requisition.repository;

import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchase_requisition.model.Inventory;

public interface InventoryRepo extends CrudRepository<Inventory, String> {

}
