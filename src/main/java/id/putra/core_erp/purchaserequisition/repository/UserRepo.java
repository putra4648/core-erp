package id.putra.core_erp.purchaserequisition.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchaserequisition.enums.StatusEnum;
import id.putra.core_erp.purchaserequisition.model.Department;
import id.putra.core_erp.purchaserequisition.model.Users;

public interface UserRepo extends CrudRepository<Users, String> {

        List<Users> findAll(Sort sort);

        Optional<Users> findByUsernameIgnoreCase(String username);

        List<Users> findByDepartment(Department department);

        @Query(nativeQuery = true, value = "select cu.* from mst_users cu join mst_depts d on cu.dept_number  = d.dept_number where cu.dept_number like '%' || ?1 || '%' and cu.group_code in (?2) or ?1 = any(cu.other_depts) order by cu.fullname ")
        List<Users> findAllDepartmentUsersById(String deptId, List<String> groupManagers);

        @Query(nativeQuery = true, value = "select cu.* from mst_users cu join mst_users_groups cg on cu.group_code = cg.code where cu.group_code = 'com.pre.director'")
        List<Users> findAllPresidentDirector();

        Page<Users> findByFullnameContainingIgnoreCaseAndStatus(String fullname, StatusEnum status,
                        Pageable pageable);

        long countByFullnameContainingIgnoreCaseAndStatus(String fullname, StatusEnum status);
}

