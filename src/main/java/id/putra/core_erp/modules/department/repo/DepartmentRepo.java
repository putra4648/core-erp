package id.putra.core_erp.modules.department.repo;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.repository.SoftDeleteRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepo implements SoftDeleteRepository<Department> {

	/**
	 * Looks up a department by its business number.
	 *
	 * <p>Queries {@code code}, not {@code deptNumber}: JPQL resolves <em>field</em> names,
	 * and the entity field is {@code code} even though its column is {@code dept_number}.
	 * A query naming the column fails at parse time with
	 * <em>"could not resolve property"</em>.
	 */
	public Optional<Department> findByDeptNumber(String deptNumber) {
		return find("code = ?1", deptNumber).firstResultOptional();
	}

	/**
	 * Prefix search on the department name, paged and sorted.
	 *
	 * <p>Queries {@code name}, matching the entity field. The trailing wildcard is
	 * appended by the caller-supplied pattern; the {@code like} operand is a prefix match,
	 * not a contains match.
	 */
	public List<Department> findAllByName(String deptName, Page page, Sort sort) {
		return find("name like ?1", sort, deptName).page(page).list();
	}

}
