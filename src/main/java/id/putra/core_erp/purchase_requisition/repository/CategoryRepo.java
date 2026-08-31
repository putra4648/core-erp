package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.shared.entity.Category;

public interface CategoryRepo extends CrudRepository<Category, Long> {

    @Query(value = """
            with recursive category_query as (
                select
                    id,
                    name,
                    parent_id
                from
                    category
                where
                    name like '%' || ?1 || '%'
            union all
                select
                    e.id,
                    e.name,
                    e.parent_id
                from
                    category e
                inner join category_query c on
                    e.parent_id = c.id
                )
            select distinct
            	*
            from
            	category_query
                        """, nativeQuery = true)
    List<Map<String, Object>> getAllCategories(String name);
}
