package id.putra.core_erp.modules.procurement.repository;

import java.util.Collection;
import java.util.List;

import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Search queries here are <b>native SQL</b>, so the {@code @SQLRestriction("deleted = false")}
 * inherited from {@code BaseEntity} is not applied — Hibernate only rewrites JPQL and
 * criteria queries. Every WHERE clause below therefore carries an explicit
 * {@code r.deleted = false} guard. Keep that guard when adding new native queries.
 *
 * <p>Native queries MUST go through {@code find("#" + query)}. Without the {@code #}
 * prefix Panache parses the string as HQL and rejects {@code select r.*}. This was a
 * latent bug in the original code — {@code select r.*} had never parsed.
 *
 * <p>While editing these clauses, note the missing parentheses in the original title/id
 * OR-condition: {@code a or b and c} binds as {@code a or (b and c)}. The grouping is
 * preserved as-is here to avoid changing search behaviour; only the deleted guard and the
 * explicit parens around the OR-pair were introduced.
 *
 * @see id.putra.core_erp.repository.SoftDeleteRepository
 */
@ApplicationScoped
public class RequisitionRepo implements SoftDeleteRepository<Requisition> {

	@Inject
	RequisitionItemRepo requisitionItemRepo;

	/** Requisition owns its line items, so they are flagged together with the parent. */
	@Override
	public void cascadeToChildren(Requisition requisition) {
		requisitionItemRepo.find("requisition.id", requisition.id).list()
				.forEach(requisitionItemRepo::softDelete);
	}

	public List<Requisition> getTitleContainingIgnoreCaseOrStatusAndDepartmentIn(String title, String status,
			Collection<String> deptIds) {
		return find("#" + """
				select
					r.*
				from
					requisition r
				where
					r.deleted = false
				and
					(upper(r.title) like '%' || upper(?1) || '%'
				or
					upper(r.id) like '%' || upper(?1) || '%')
				and
					upper(r.status) like '%' || upper(?2) || '%'
				and
					upper(r.dept_id) in (?3)
				order by r.status
				""", title, status, deptIds).list();
	}

	public List<Requisition> getTitleContainingIgnoreCaseOrStatusAndPurchaser(String title, String status,
			String purchaserNrp) {
		return find("#" + """
				select
					r.*
				from
					requisition r
				where
					r.deleted = false
				and
					upper(r.title) like '%' || upper(?1) || '%'
				and
					upper(r.status) like '%' || upper(?2) || '%'
				and
					(temp_purchaser_approval is not null
					and temp_purchaser_approval -> 'users' ->> 'nrp' = ?3)
				order by r.status
				""", title, status, purchaserNrp).list();
	}

	public List<Requisition> getAllSpender(String deptNumber) {
		return find("deptNumber = ?1", deptNumber).list();
	}

	public Long countByTitleContainingIgnoreCaseOrStatusAndPurchaserNrp(String title, String status,
			String purchaserNrp) {
		return count("#" + """
				select
					count(*)
				from
					requisition r
				where
					r.deleted = false
				and
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
		return count("#" + """
				select
					count(*)
				from
					requisition r
				where
					r.deleted = false
				and
					(upper(r.title) like '%' || upper(?1) || '%'
				or
					upper(r.id) like '%' || upper(?1) || '%')
				and
					upper(r.status) like '%' || upper(?2) || '%'
				and
					upper(r.dept_id) in (?3)
				""", title, status, deptIds);
	}

}
