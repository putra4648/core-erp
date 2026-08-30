package id.putra.core_erp.purchase_requisition.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchase_requisition.model.Requisition;

public interface RequisitionRepo extends CrudRepository<Requisition, String> {

        @Query(nativeQuery = true, value = """
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
                                                """)
        List<Requisition> getTitleContainingIgnoreCaseOrStatusAndDepartmentIn(String title,
                        String status,
                        Collection<String> deptIds);

        @Query(nativeQuery = true, value = """
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
                                                      """)
        List<Requisition> getTitleContainingIgnoreCaseOrStatusAndPurchaser(String title,
                        String status,
                        String purchaserNrp);

        @Query(nativeQuery = true, value = """
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
                                                                              """)
        Long countByTitleContainingIgnoreCaseOrStatusAndPurchaserNrp(String title,
                        String status,
                        String purchaserNrp);

        @Query(nativeQuery = true, value = """
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
                                                """)
        Long countByTitleContainingIgnoreCaseOrStatusAndDepartmentIn(String title, String status,
                        Collection<String> deptIds);

        @Query(value = """
                        select
                        	date_trunc('year',current_date) + (n * '1 month'::interval) as month_name,
                        	count(r.title) as total
                        from
                        	generate_series(3,
                        	14) as n
                        left join requisition r on
                        	r.budget_date = date_trunc('year',current_date) + (n * '1 month'::interval)
                        and
                                r.dept_id = ?1
                        group by
                                n.n
                        ORDER BY
                                n.n
                                                """, nativeQuery = true)
        List<Map<String, Object>> getAllSpender(String deptId);

}
