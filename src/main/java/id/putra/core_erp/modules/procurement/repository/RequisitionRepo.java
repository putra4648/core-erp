package id.putra.core_erp.modules.procurement.repository;

import java.util.Collection;
import java.util.List;

import id.putra.core_erp.modules.procurement.model.Requisition;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RequisitionRepo implements PanacheRepository<Requisition> {

	public List<Requisition> getTitleContainingIgnoreCaseOrStatusAndDepartmentIn(String title, String status,
			Collection<String> deptIds) {
		return find("""
				select
					r.*
				from
					requisition r
				where
					upper(r.title) like '%' || upper(?1) || '%'
				or
				        upper(r.id) like '%' || upper(?1) || '%'
				and
					upper(r.status) like '%' || upper(?2) || '%'
				and
				        upper(r.dept_id) in (?3)
				order by r.status
				                        """, title, status, deptIds).list();
	}

	public List<Requisition> getTitleContainingIgnoreCaseOrStatusAndPurchaser(String title, String status,
			String purchaserNrp) {
		return find("""
				select
					r.*
				from
					requisition r
				where
					upper(r.title) like '%' || upper(?1) || '%'
					and
				                        	upper(r.status) like '%' || upper(?2) || '%'
					and
						(temp_purchaser_approval is not null
						and temp_purchaser_approval -> 'users' ->> 'nrp' = ?3
				                )
				      order by r.status
				                              """, title, status, purchaserNrp).list();
	}

	public List<Requisition> getAllSpender(String deptNumber) {
		return find("deptNumber = ?1", deptNumber).list();
	}

	public Long countByTitleContainingIgnoreCaseOrStatusAndPurchaserNrp(String title, String status,
			String purchaserNrp) {
		return count("""
				select
					count(r)
				from
					requisition r
				where
					upper(r.title) like '%' || upper(?1) || '%'
					and
				                        	upper(r.status) like '%' || upper(?2) || '%'
					and
						(temp_purchaser_approval is not null
						and temp_purchaser_approval -> 'users' ->> 'nrp' = ?3)
				                                                      """, title, status, purchaserNrp);
	}

	public Long countByTitleContainingIgnoreCaseOrStatusAndDepartmentIn(String title, String status,
			Collection<String> deptIds) {
		return count("""
				select
					count(r)
				from
					requisition r
				where
					upper(r.title) like '%' || upper(?1) || '%'
				or
				        upper(r.id) like '%' || upper(?1) || '%'
				and
					upper(r.status) like '%' || upper(?2) || '%'
				and
				        upper(r.dept_id) in (?3)
				                        """, title, status, deptIds);
	}

}
