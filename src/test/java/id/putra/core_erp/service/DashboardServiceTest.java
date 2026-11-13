package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.modules.procurement.repository.RequisitionItemRepo;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.service.DashboardService;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link DashboardService} — the aggregation behind both dashboards.
 *
 * <p>
 * The spender roll-up is the fragile part: it groups by department
 * <em>name</em>, skips
 * requisitions with no department, and must keep the totals consistent with the
 * per-row
 * line-item arithmetic. If the skip is dropped, the whole aggregation NPEs on
 * one bad row;
 * if the per-row total is wrong, every department's figure is wrong.
 */
@QuarkusTest
class DashboardServiceTest {

	@Inject
	DashboardService dashboardService;

	@Inject
	RequisitionRepo requisitionRepo;

	@Inject
	RequisitionItemRepo requisitionItemRepo;

	@Inject
	DepartmentRepo departmentRepo;

	@Inject
	BudgetRepo budgetRepo;

	@Inject
	EntityManager em;

	private static final AtomicInteger SEQ = new AtomicInteger();

	private Department newDepartment() {
		Department d = TestFixtures.department("DSH" + SEQ.incrementAndGet());
		departmentRepo.persist(d);
		return d;
	}

	private Budget newBudget(Department dept) {
		Budget b = TestFixtures.budget(dept, "Budget " + dept.code);
		budgetRepo.persist(b);
		return b;
	}

	/**
	 * A requisition with one line worth {@code qty * unitPrice}, in the given
	 * status.
	 */
	private Requisition newRequisition(Department dept, ApprovalStatus status, int qty, String unitPrice) {
		String ref = "DSH-REF-" + SEQ.incrementAndGet();
		Requisition r = TestFixtures.requisition(ref, dept, newBudget(dept));
		r.referenceId = ref;
		r.status = status;
		requisitionRepo.persist(r);

		RequisitionItem item = TestFixtures.requisitionItem(r, "Item " + ref, qty, unitPrice);
		requisitionItemRepo.persist(item);
		em.flush();
		em.clear();
		return requisitionRepo.find("referenceId", ref).firstResult();
	}

	// ------------------------------------------------------------- counts
	@Test
	@TestTransaction
	@DisplayName("countRequisitions counts every live requisition")
	void countRequisitionsCountsAll() {
		Department dept = newDepartment();
		long before = dashboardService.countRequisitions();

		newRequisition(dept, ApprovalStatus.SUBMIT, 1, "10.00");
		newRequisition(dept, ApprovalStatus.APPROVED, 1, "10.00");

		assertEquals(before + 2, dashboardService.countRequisitions(),
				"both requisitions must count regardless of status");
	}

	@Test
	@TestTransaction
	@DisplayName("countRequisitionsByStatus counts only that status")
	void countByStatusFiltersStatus() {
		Department dept = newDepartment();
		long submitBefore = dashboardService.countRequisitionsByStatus(ApprovalStatus.SUBMIT);
		long approvedBefore = dashboardService.countRequisitionsByStatus(ApprovalStatus.APPROVED);

		newRequisition(dept, ApprovalStatus.SUBMIT, 1, "10.00");
		newRequisition(dept, ApprovalStatus.APPROVED, 1, "10.00");
		newRequisition(dept, ApprovalStatus.APPROVED, 1, "10.00");

		assertEquals(submitBefore + 1, dashboardService.countRequisitionsByStatus(ApprovalStatus.SUBMIT),
				"the SUBMIT bucket must only gain the SUBMIT row");
		assertEquals(approvedBefore + 2, dashboardService.countRequisitionsByStatus(ApprovalStatus.APPROVED),
				"the APPROVED bucket must only gain the APPROVED rows");
	}

	@Test
	@TestTransaction
	@DisplayName("soft delete removes a requisition from both counts")
	void softDeleteRemovesFromCounts() {
		Department dept = newDepartment();
		Requisition r = newRequisition(dept, ApprovalStatus.SUBMIT, 1, "10.00");

		long totalBefore = dashboardService.countRequisitions();
		long submitBefore = dashboardService.countRequisitionsByStatus(ApprovalStatus.SUBMIT);

		requisitionRepo.softDeleteById(r.id);
		em.flush();
		em.clear();

		assertEquals(totalBefore - 1, dashboardService.countRequisitions(),
				"a soft-deleted requisition must leave the total");
		assertEquals(submitBefore - 1, dashboardService.countRequisitionsByStatus(ApprovalStatus.SUBMIT),
				"a soft-deleted requisition must leave its status bucket too");
	}

	// ------------------------------------------------------- spender roll-up

	@Test
	@TestTransaction
	@DisplayName("getAllSpender totals qty x unitPrice per department")
	void spenderTotalsPerDepartment() {
		Department dept = newDepartment();
		// 3 x 10.00 = 30.00 plus 2 x 5.00 = 10.00 -> 40.00 for this department.
		newRequisition(dept, ApprovalStatus.SUBMIT, 3, "10.00");
		newRequisition(dept, ApprovalStatus.SUBMIT, 2, "5.00");

		List<Map<String, Object>> rows = dashboardService.getAllSpender();
		Map<String, Object> mine = rows.stream()
				.filter(m -> dept.name.equals(m.get("dept_name")))
				.findFirst()
				.orElseThrow(() -> new AssertionError("the department must appear in the roll-up"));

		assertEquals(0, new BigDecimal("40.00").compareTo((BigDecimal) mine.get("total_amount")),
				"the total must sum qty * unitPrice across the department's line items");
		assertEquals(2L, mine.get("count_pr"), "the requisition count must reflect both rows");
	}

	@Test
	@TestTransaction
	@DisplayName("getAllSpender is sorted by total spend, highest first")
	void spenderIsSortedDescending() {
		Department small = newDepartment();
		Department large = newDepartment();
		newRequisition(small, ApprovalStatus.SUBMIT, 1, "1.00");
		newRequisition(large, ApprovalStatus.SUBMIT, 1, "9999.00");

		List<Map<String, Object>> rows = dashboardService.getAllSpender();

		int smallIdx = indexOfDept(rows, small.name);
		int largeIdx = indexOfDept(rows, large.name);
		assertTrue(largeIdx >= 0 && smallIdx >= 0, "both departments must appear");
		assertTrue(largeIdx < smallIdx,
				"the bigger spender must sort first - the dashboard shows a top-N list");
	}

	@Test
	@TestTransaction
	@DisplayName("getAllSpender sums across departments independently")
	void spenderGroupsPerDepartment() {
		Department a = newDepartment();
		Department b = newDepartment();
		newRequisition(a, ApprovalStatus.SUBMIT, 2, "10.00"); // 20.00
		newRequisition(b, ApprovalStatus.SUBMIT, 2, "30.00"); // 60.00

		List<Map<String, Object>> rows = dashboardService.getAllSpender();

		assertEquals(0, new BigDecimal("20.00").compareTo(amountOf(rows, a.name)),
				"department A's total must not absorb department B's spend");
		assertEquals(0, new BigDecimal("60.00").compareTo(amountOf(rows, b.name)),
				"department B's total must not absorb department A's spend");
	}

	private static int indexOfDept(List<Map<String, Object>> rows, String deptName) {
		for (int i = 0; i < rows.size(); i++) {
			if (deptName.equals(rows.get(i).get("dept_name"))) {
				return i;
			}
		}
		return -1;
	}

	private static BigDecimal amountOf(List<Map<String, Object>> rows, String deptName) {
		return rows.stream()
				.filter(m -> deptName.equals(m.get("dept_name")))
				.map(m -> (BigDecimal) m.get("total_amount"))
				.findFirst()
				.orElseThrow(() -> new AssertionError("department not present: " + deptName));
	}

	// ------------------------------------------------------ recent requisitions

	@Test
	@TestTransaction
	@DisplayName("getRecentRequisitions honours the limit")
	void recentHonoursLimit() {
		Department dept = newDepartment();
		for (int i = 0; i < 3; i++) {
			newRequisition(dept, ApprovalStatus.SUBMIT, 1, "1.00");
		}

		assertTrue(dashboardService.getRecentRequisitions(2).size() <= 2,
				"the limit must be applied - the dashboard shows a fixed-size list");
	}

	@Test
	@TestTransaction
	@DisplayName("getRecentRequisitions never returns more than the limit")
	void recentNeverExceedsLimit() {
		Department dept = newDepartment();
		for (int i = 0; i < 5; i++) {
			newRequisition(dept, ApprovalStatus.SUBMIT, 1, "1.00");
		}

		List<Requisition> recent = dashboardService.getRecentRequisitions(3);

		assertEquals(3, recent.size(), "with more rows than the limit, exactly the limit is returned");
		recent.forEach(r -> assertNotNull(r.id, "every returned requisition must be a persisted row"));
	}
}
