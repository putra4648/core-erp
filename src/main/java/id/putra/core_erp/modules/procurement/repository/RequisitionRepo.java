package id.putra.core_erp.modules.procurement.repository;

import java.util.Collection;
import java.util.List;

import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Requisition search queries.
 *
 * <p>Two query styles live here, and the difference matters:
 *
 * <ul>
 *   <li><b>JPQL</b> for every query whose columns are mapped on {@link Requisition}.
 *       {@code @SQLRestriction} on {@code BaseEntity} is applied to JPQL automatically,
 *       so the {@code deleted = false} guard is inherited and cannot be forgotten.</li>
 *   <li><b>Native SQL</b> only for the purchaser queries, which read
 *       {@code temp_purchaser_approval} — a column that exists in the database but is
 *       not mapped on the entity. Those queries carry an explicit
 *       {@code r.deleted = false} written by hand, and go through
 *       {@code createNativeQuery(sql, Requisition.class)} rather than Panache's
 *       {@code find(...)}.</li>
 * </ul>
 *
 * <p>Why {@code select r.*} fails with {@code find("#" + query)}: Panache builds a native
 * query with an entity result mapping, and Hibernate validates each projection against the
 * entity metamodel. {@code r.*} is not a mapped attribute, so parsing failed with
 * <em>"must be defined on your JPA entity or one of its super classes"</em>.
 * {@code createNativeQuery(sql, Entity.class)} avoids that because Hibernate maps result
 * columns to the entity instead of validating the select list as attribute paths.
 *
 * <p>The department join is on {@code Department.dept_number}, not on its surrogate id,
 * so callers pass department <em>numbers</em>.
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

	/**
	 * Filters by title (or id) and status, restricted to the given department numbers.
	 *
	 * <p>The title/id condition is grouped so the OR does not swallow the remaining
	 * predicates: {@code (title like ? or id like ?) and status like ? and dept in (?)}.
	 * Without the parentheses the status and department filters would only bind to the
	 * right-hand side of the OR, returning far more rows than intended.
	 *
	 * @param title   free-text fragment matched against title and id, case-insensitive
	 * @param status  status fragment, case-insensitive
	 * @param deptIds department <em>numbers</em>, matching {@code Department.dept_number}
	 * @return matching requisitions, ordered by status
	 */
	public List<Requisition> getTitleContainingIgnoreCaseOrStatusAndDepartmentIn(String title, String status,
			Collection<String> deptIds) {
		return find("""
				select r
				from Requisition r
				where
					(
						upper(r.title) like concat('%', upper(?1), '%')
					or
						upper(cast(r.id as string)) like concat('%', upper(?1), '%')
					)
				and
					upper(cast(r.status as string)) like concat('%', upper(?2), '%')
				and
					r.department.code in ?3
				order by r.status
				""", title, status, deptIds).list();
	}

	/**
	 * Filters by title and status for requisitions awaiting a specific purchaser's
	 * approval, read from the {@code temp_purchaser_approval} JSON column.
	 *
	 * <p><b>Native SQL, deliberately.</b> That column is not mapped on
	 * {@code Requisition} — it exists only in the database — so JPQL cannot express it.
	 * The {@code deleted = false} guard is therefore written out by hand here; the
	 * inherited {@code @SQLRestriction} does not cover native statements.
	 *
	 * <p>The query returns entities through {@code createNativeQuery(sql, Requisition.class)},
	 * which maps result columns onto the entity. {@code select r.*} is correct in that form.
	 *
	 * @param title        free-text fragment matched against the title, case-insensitive
	 * @param status       status fragment, case-insensitive
	 * @param purchaserNrp purchaser NRP looked up inside {@code temp_purchaser_approval}
	 * @return matching requisitions, ordered by status
	 */
	@SuppressWarnings("unchecked")
	public List<Requisition> getTitleContainingIgnoreCaseOrStatusAndPurchaser(String title, String status,
			String purchaserNrp) {
		return getEntityManager().createNativeQuery("""
				select r.*
				from requisition r
				where
					r.deleted = false
				and
					upper(r.title) like concat('%', upper(?1), '%')
				and
					upper(r.status) like concat('%', upper(?2), '%')
				and
					r.temp_purchaser_approval is not null
				and
					r.temp_purchaser_approval -> 'users' ->> 'nrp' = ?3
				order by r.status
				""", Requisition.class)
				.setParameter(1, title)
				.setParameter(2, status)
				.setParameter(3, purchaserNrp)
				.getResultList();
	}

	/** Every requisition belonging to the given department number. */
	public List<Requisition> getAllSpender(String deptNumber) {
		return find("department.code = ?1", deptNumber).list();
	}

	/**
	 * Count counterpart of {@link #getTitleContainingIgnoreCaseOrStatusAndPurchaser}.
	 *
	 * <p>Native SQL for the same reason as its sibling: {@code temp_purchaser_approval}
	 * is not an entity attribute. The soft delete guard is explicit.
	 */
	public Long countByTitleContainingIgnoreCaseOrStatusAndPurchaserNrp(String title, String status,
			String purchaserNrp) {
		Number result = (Number) getEntityManager().createNativeQuery("""
				select count(*)
				from requisition r
				where
					r.deleted = false
				and
					upper(r.title) like concat('%', upper(?1), '%')
				and
					upper(r.status) like concat('%', upper(?2), '%')
				and
					r.temp_purchaser_approval is not null
				and
					r.temp_purchaser_approval -> 'users' ->> 'nrp' = ?3
				""")
				.setParameter(1, title)
				.setParameter(2, status)
				.setParameter(3, purchaserNrp)
				.getSingleResult();
		return result == null ? 0L : result.longValue();
	}

	/** Count counterpart of {@link #getTitleContainingIgnoreCaseOrStatusAndDepartmentIn}. */
	public Long countByTitleContainingIgnoreCaseOrStatusAndDepartmentIn(String title, String status,
			Collection<String> deptIds) {
		return count("""
				select count(r)
				from Requisition r
				where
					(
						upper(r.title) like concat('%', upper(?1), '%')
					or
						upper(cast(r.id as string)) like concat('%', upper(?1), '%')
					)
				and
					upper(cast(r.status as string)) like concat('%', upper(?2), '%')
				and
					r.department.code in ?3
				""", title, status, deptIds);
	}

}
