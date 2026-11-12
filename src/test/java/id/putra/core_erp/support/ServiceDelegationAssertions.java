package id.putra.core_erp.support;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import id.putra.core_erp.repository.SoftDeleteRepository;

/**
 * Shared assertions for the one-repository CRUD services.
 *
 * <p>Most module services are a thin pass-through: {@code save} persists, {@code delete}
 * soft-deletes, {@code count} counts. The tests are therefore identical in shape and only
 * differ by which repository is mocked, so the body lives here and each
 * {@code *ServiceTest} supplies its repository mock.
 *
 * <p><b>The one assertion that matters.</b> {@link #assertSoftDeleteUsed} pins down that
 * {@code delete} routes to {@code softDeleteById} and never to Panache's {@code deleteById}.
 * That is not redundant with the compiler: both methods exist on
 * {@link SoftDeleteRepository}, both take a {@code Long}, and picking the wrong one silently
 * emits a real SQL {@code DELETE} instead of flipping the flag. Nothing fails until
 * production rows have already gone.
 */
public final class ServiceDelegationAssertions {

	private ServiceDelegationAssertions() {
	}

	/**
	 * Asserts the service's {@code delete} reached {@code softDeleteById} and did not touch
	 * either hard-delete path.
	 *
	 * @param repository the mocked repository behind the service
	 * @param id         the id the service was asked to delete
	 */
	public static void assertSoftDeleteUsed(SoftDeleteRepository<?> repository, Long id) {
		verify(repository).softDeleteById(id);
		verify(repository, never()).deleteById(id);
	}

	/**
	 * Asserts a delegation reads the number of rows through {@code count()} rather than
	 * loading every row and sizing the list — the difference matters on a large table.
	 *
	 * @param repository the mocked repository behind the service
	 */
	public static void assertCountNotLoadAll(SoftDeleteRepository<?> repository) {
		verify(repository).count();
		verify(repository, never()).listAll();
		verify(repository, never()).findAll();
	}
}
