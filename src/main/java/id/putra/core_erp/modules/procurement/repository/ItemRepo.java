package id.putra.core_erp.modules.procurement.repository;

import java.util.Optional;

import id.putra.core_erp.modules.procurement.model.Item;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemRepo implements PanacheRepository<Item> {

	public long countByNameContainingIgnoreCase(String name) {
		return count("name ilike ?1", name);
	}

	public Optional<Item> findByNameEqualsIgnoreCase(String name) {
		return find("name ilike ?1", name).firstResultOptional();
	}

	public Optional<Item> findByItemNumber(String name) {
		return find("itemNumber ilike ?1", name).firstResultOptional();
	}

}
