package id.putra.core_erp.modules.procurement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DashboardService {

    private final RequisitionRepo requisitionRepo;

    public DashboardService(RequisitionRepo requisitionRepo) {
        this.requisitionRepo = requisitionRepo;
    }
    public List<Map<String, Object>> getAllSpender() {
        List<Map<String, Object>> dtos = new ArrayList<>();

        // Optional<Users> users = userRepo
        // .findByUsernameIgnoreCase(authentication != null ? authentication.getName() :
        // "");
        // if (users.isPresent() && users.get().getDepartment() != null) {
        // String deptNumber = users.get().getDepartment().getDeptNumber();

        // requisitionRepo.getAllSpender(deptNumber).forEach(entry -> {
        // Map<String, Object> data = new HashMap<>();
        // data.put("month", sdf.format(Date.from((Instant) entry.get("month_name"))));
        // data.put("total", entry.get("total"));
        // dtos.add(data);
        // });
        // } else {
        // // Nilai default jika tidak ada users atau department
        // Map<String, Object> defaultData = new HashMap<>();
        // defaultData.put("month", "N/A");
        // defaultData.put("total", 0);
        // dtos.add(defaultData);
        // }
        return dtos;
    }
}
