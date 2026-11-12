package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.putra.core_erp.modules.procurement.model.Transfer;
import id.putra.core_erp.modules.procurement.repository.TransferRequestRepo;
import id.putra.core_erp.modules.procurement.service.TransferService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link TransferService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

	@Mock
	TransferRequestRepo transferRequestRepo;

	@InjectMocks
	TransferService transferService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Transfer entity = new Transfer();

		transferService.save(entity);

		verify(transferRequestRepo).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		transferService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(transferRequestRepo, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Transfer entity = new Transfer();
		given(transferRequestRepo.findById(3L)).willReturn(entity);

		assertSame(entity, transferService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(transferRequestRepo.count()).willReturn(42L);

		assertSame(42L, transferService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(transferRequestRepo);
	}

	// ------------------------------------------- findAll(Transfer) wildcard
	// defaulting

	@Test
	@DisplayName("a blank status falls back to %% so an empty filter matches everything")
	void blankStatusBecomesDoubleWildcard() {
		Transfer filter = new Transfer();
		filter.status = "";

		transferService.findAll(filter);

		verify(transferRequestRepo).findByStatusLikeAndIdLike("%%", null);
	}

	@Test
	@DisplayName("a null status falls back to %%")
	void nullStatusBecomesDoubleWildcard() {
		Transfer filter = new Transfer();
		filter.status = null;

		transferService.findAll(filter);

		verify(transferRequestRepo).findByStatusLikeAndIdLike("%%", null);
	}

	@Test
	@DisplayName("a supplied status is passed through unchanged")
	void suppliedStatusPassesThrough() {
		Transfer filter = new Transfer();
		filter.status = "DRAFT";

		transferService.findAll(filter);

		verify(transferRequestRepo).findByStatusLikeAndIdLike("DRAFT", null);
	}

	@Test
	@DisplayName("a null dto does not throw and searches with both wildcards")
	void nullDtoUsesWildcards() {
		transferService.findAll((Transfer) null);

		verify(transferRequestRepo).findByStatusLikeAndIdLike("%%", null);
	}

	@Test
	@DisplayName("a dto with an id narrows the id operand to that id")
	void idNarrowsTheIdOperand() {
		Transfer filter = new Transfer();
		filter.id = 42L;
		filter.status = "SENT";

		transferService.findAll(filter);

		verify(transferRequestRepo).findByStatusLikeAndIdLike("SENT", 42L);
	}

	@Test
	@DisplayName("getById delegates to the repository using the dto id")
	void getByIdDelegates() {
		Transfer filter = new Transfer();
		filter.id = 9L;
		Transfer found = new Transfer();
		given(transferRequestRepo.findById(9L)).willReturn(found);

		assertSame(found, transferService.getById(filter));

	}
}
