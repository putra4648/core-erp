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

import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.inventory.repository.InventoryItemRepository;
import id.putra.core_erp.modules.inventory.service.InventoryItemCoreService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link InventoryItemCoreService} carries no business
 * rules, so there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class InventoryItemCoreServiceTest {

	@Mock
	InventoryItemRepository inventoryItemRepository;

	@InjectMocks
	InventoryItemCoreService inventoryItemCoreService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		InventoryItem entity = new InventoryItem();

		inventoryItemCoreService.save(entity);

		verify(inventoryItemRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		inventoryItemCoreService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(inventoryItemRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		InventoryItem entity = new InventoryItem();
		given(inventoryItemRepository.findById(3L)).willReturn(entity);

		assertSame(entity, inventoryItemCoreService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(inventoryItemRepository.count()).willReturn(42L);

		assertSame(42L, inventoryItemCoreService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(inventoryItemRepository);
	}
}
