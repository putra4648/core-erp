package id.putra.core_erp.shared.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import id.putra.core_erp.shared.entity.ParameterLookup;

public interface ParameterLookupRepository extends JpaRepository<ParameterLookup, Long> {
    ParameterLookup findByIdOrKey(Long id, String key);

    @Query("""
            SELECT p FROM ParameterLookup p WHERE p.key LIKE %:search% OR p.value LIKE %:search% OR p.description LIKE %:search%
            """)
    Page<ParameterLookup> findAll(String search, Pageable pageable);
}
