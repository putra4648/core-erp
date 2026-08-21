package id.putra.core_erp.warehouse.module.warehouse.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

}

