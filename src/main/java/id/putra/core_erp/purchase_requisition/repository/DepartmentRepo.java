package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import id.putra.core_erp.purchase_requisition.model.Department;

public interface DepartmentRepo extends CrudRepository<Department, String> {

    List<Department> findByStatus(StatusEnum status);

    Department findByDeptNameStartingWithIgnoreCase(String deptName);

    List<Department> findAllByDeptNameContainingIgnoreCaseAndStatus(String deptName, StatusEnum status, Sort sort);

    long countByDeptNameContainingIgnoreCaseAndStatus(String deptName, StatusEnum status);

}
