package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.modules.procurement.repository.ItemRepo;
import id.putra.core_erp.modules.procurement.service.ItemService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link ItemService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	@Mock
	ItemRepo itemRepo;

	@InjectMocks
	ItemService itemService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Item entity = new Item();

		itemService.save(entity);

		verify(itemRepo).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		itemService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(itemRepo, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Item entity = new Item();
		given(itemRepo.findById(3L)).willReturn(entity);

		assertSame(entity, itemService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(itemRepo.count()).willReturn(42L);

		assertSame(42L, itemService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(itemRepo);
	}
}
