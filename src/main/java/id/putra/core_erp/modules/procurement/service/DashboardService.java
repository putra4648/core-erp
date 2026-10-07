package id.putra.core_erp.modules.procurement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DashboardService {

	public DashboardService(RequisitionRepo requisitionRepo) {
	}

	public List<Map<String, Object>> getAllSpender() {
		List<Map<String, Object>> dtos = new ArrayList<>();

		return dtos;
	}
}
