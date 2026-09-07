package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;

import id.putra.core_erp.purchase_requisition.model.Transfer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferRequestRepo implements PanacheRepository<Transfer> {

    public List<Transfer> findByStatusLikeAndIdLike(String status, String id) {
        return list("status like ?1 and id like ?2", status, id);
    }
}
