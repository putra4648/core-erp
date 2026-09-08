package id.putra.core_erp.purchase_requisition.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import id.putra.core_erp.shared.entity.Category;

@ApplicationScoped
public class CategoryRepo implements PanacheRepository<Category> {

    // public List<Map<String, Object>> getAllCategories(String name) {
    //     return find("""
    //             with recursive category_query as (
    //                 select
    //                     id,
    //                     name,
    //                     parent_id
    //                 from
    //                     category
    //                 where
    //                     name like '%' || ?1 || '%'
    //             union all
    //                 select
    //                     e.id,
    //                     e.name,
    //                     e.parent_id
    //                 from
    //                     category e
    //                 inner join category_query c on
    //                     e.parent_id = c.id
    //                 )
    //             select distinct
    //                 *
    //             from
    //                 category_query
    //             """, name).list();
    // }
}
