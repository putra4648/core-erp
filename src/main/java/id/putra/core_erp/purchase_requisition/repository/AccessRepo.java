package id.putra.core_erp.purchase_requisition.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchase_requisition.model.Access;

import java.util.List;

public interface AccessRepo extends CrudRepository<Access, String> {
    List<Access> findAll(Sort sort);
}
