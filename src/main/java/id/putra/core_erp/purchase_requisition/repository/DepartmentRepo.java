package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import id.putra.core_erp.purchase_requisition.model.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepo implements PanacheRepository<Department> {

    public List<Department> findByStatus(StatusEnum status) {
        return find("status", status).list();
    }

    public Optional<Department> findByDeptNumber(String deptNumber) {
        return find("deptNumber", deptNumber).firstResultOptional();
    }

    public Department findByDeptNameStartingWithIgnoreCase(String deptName) {
        return find("deptName like ?1%", deptName).firstResult();
    }

    public List<Department> findAllByDeptNameContainingIgnoreCaseAndStatus(String deptName, StatusEnum status,
            Sort sort) {
        return find("deptName like ?1% and status = ?2", deptName, status).list();
    }

    public long countByDeptNameContainingIgnoreCaseAndStatus(String deptName, StatusEnum status) {
        return count("deptName like ?1% and status = ?2", deptName, status);
    }

}
