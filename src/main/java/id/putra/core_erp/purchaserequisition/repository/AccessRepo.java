package id.putra.core_erp.purchaserequisition.repository;

import id.putra.core_erp.purchaserequisition.model.Access;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccessRepo extends CrudRepository<Access, String> {
    List<Access> findAll(Sort sort);
}

