package id.putra.core_erp.warehouse.module.inbound.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import id.putra.core_erp.warehouse.module.inbound.model.entity.QualityInspection;

public interface QualityInspectionRepository extends JpaRepository<QualityInspection, Long>, JpaSpecificationExecutor<QualityInspection> {

}

