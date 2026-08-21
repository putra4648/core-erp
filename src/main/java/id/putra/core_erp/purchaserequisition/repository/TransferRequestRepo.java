package id.putra.core_erp.purchaserequisition.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchaserequisition.model.Transfer;

public interface TransferRequestRepo extends CrudRepository<Transfer, String> {
    List<Transfer> findByStatusLikeAndIdLike(String status, String id);
}

