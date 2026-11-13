package id.putra.core_erp.modules.procurement.repository;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.procurement.model.Transfer;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferRequestRepo implements SoftDeleteRepository<Transfer> {

	/**
	 * Wildcard search on status and id.
	 *
	 * <p>
	 * Both operands are {@code like} patterns supplied by the caller;
	 * {@code TransferService}
	 * passes {@code "%%"} when the filter is blank, which matches every row.
	 */
	public List<Transfer> findByStatusLikeAndIdLike(String status, Long id) {
		StringBuilder sb = new StringBuilder("status like ?1");

		if (id != null) {
			sb.append(" and id = ?2");
			return list(sb.toString(), status, id);
		}
		return list(sb.toString(), status);
	}

	/**
	 * Looks up a transfer by its title.
	 *
	 * <p>
	 * Queries {@code title}: {@link Transfer} has no {@code reqNo} field, so the
	 * previous
	 * query on that property failed at parse time with <em>"could not resolve
	 * property"</em>.
	 * {@code singleResultOptional} is deliberate — a non-unique match raises rather
	 * than
	 * silently returning the first row, which is the safer default for a lookup by
	 * identity.
	 */
	public Optional<Transfer> findByTitle(String title) {
		return find("title like ?1", title).singleResultOptional();
	}
}
