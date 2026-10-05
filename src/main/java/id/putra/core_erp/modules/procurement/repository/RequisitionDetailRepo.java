package id.putra.core_erp.modules.procurement.repository;

import java.util.List;

import id.putra.core_erp.modules.procurement.model.RequisitionDetail;
import id.putra.core_erp.modules.supplier.model.Supplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RequisitionDetailRepo implements PanacheRepository<RequisitionDetail> {

	public List<RequisitionDetail> findBySupplier(Supplier supplier) {
		return find("supplier", supplier).list();
	}

}
