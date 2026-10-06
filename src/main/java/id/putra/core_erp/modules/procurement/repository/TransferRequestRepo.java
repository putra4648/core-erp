package id.putra.core_erp.modules.procurement.repository;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.procurement.model.Transfer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferRequestRepo implements PanacheRepository<Transfer> {

<<<<<<< HEAD
	public Optional<Transfer> findByRequestNo(String reqNo) {
		return find("reqNo like ?1", reqNo).singleResultOptional();
	}

	public List<Transfer> findByStatusLikeAndIdLike(String status, String id) {
		return list("status like ?1 and id like ?2", status, id);
=======
	public List<Transfer> findByStatusLikeAndIdLike(String status, String id) {
		return list("status like ?1 and id like ?2", status, id);
	}

	public Optional<Transfer> findByRequestNo(String reqNo) {
		return find("reqNo like ?1", reqNo).singleResultOptional();
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
