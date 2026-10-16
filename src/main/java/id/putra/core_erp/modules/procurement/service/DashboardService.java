package id.putra.core_erp.modules.procurement.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DashboardService {

	private final RequisitionRepo requisitionRepo;

	public DashboardService(RequisitionRepo requisitionRepo) {
		this.requisitionRepo = requisitionRepo;
	}

	/**
	 * Aggregates requisition spend by department, sorted by total spend
	 * descending. Each map entry contains:
	 * <ul>
	 *   <li>{@code dept_name} – department name (String)</li>
	 *   <li>{@code total_amount} – sum of all line-item totals (BigDecimal)</li>
	 *   <li>{@code count_pr} – number of requisitions (Long)</li>
	 * </ul>
	 */
	public List<Map<String, Object>> getAllSpender() {
		List<Requisition> requisitions = requisitionRepo.findAll().list();

		Map<String, Map<String, Object>> byDept = new LinkedHashMap<>();
		for (Requisition r : requisitions) {
			if (r.department == null) {
				continue;
			}
			String deptName = r.department.name;

			Map<String, Object> dept = byDept.computeIfAbsent(deptName, k -> {
				Map<String, Object> m = new LinkedHashMap<>();
				m.put("dept_name", k);
				m.put("total_amount", BigDecimal.ZERO);
				m.put("count_pr", 0L);
				return m;
			});

			BigDecimal reqTotal = calculateTotal(r);
			dept.put("total_amount", ((BigDecimal) dept.get("total_amount")).add(reqTotal));
			dept.put("count_pr", (Long) dept.get("count_pr") + 1);
		}

		return byDept.values().stream()
				.sorted((a, b) -> ((BigDecimal) b.get("total_amount"))
						.compareTo((BigDecimal) a.get("total_amount")))
				.collect(Collectors.toList());
	}

	/**
	 * Returns the most recently created requisitions, sorted by creation
	 * timestamp descending.
	 *
	 * @param limit maximum number of requisitions to return
	 * @return a list of the latest requisitions
	 */
	public List<Requisition> getRecentRequisitions(int limit) {
		return requisitionRepo.findAll().list().stream()
				.sorted(Comparator.comparing((Requisition r) -> r.createAt,
						Comparator.nullsLast(Comparator.reverseOrder())))
				.limit(limit)
				.collect(Collectors.toList());
	}

	private BigDecimal calculateTotal(Requisition requisition) {
		BigDecimal total = BigDecimal.ZERO;
		if (requisition.items != null) {
			for (var item : requisition.items) {
				if (item.unitPrice != null && item.quantity != null) {
					total = total.add(item.unitPrice.multiply(BigDecimal.valueOf(item.quantity)));
				}
			}
		}
		return total;
	}
}
