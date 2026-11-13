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

import id.putra.core_erp.modules.outbound.model.SalesOrder;
import id.putra.core_erp.modules.outbound.repository.SalesOrderRepository;
import id.putra.core_erp.modules.outbound.service.SalesOrderService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link SalesOrderService} carries no business rules,
 * so there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class SalesOrderServiceTest {

	@Mock
	SalesOrderRepository salesOrderRepository;

	@InjectMocks
	SalesOrderService salesOrderService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		SalesOrder entity = new SalesOrder();

		salesOrderService.save(entity);

		verify(salesOrderRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		salesOrderService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(salesOrderRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		SalesOrder entity = new SalesOrder();
		given(salesOrderRepository.findById(3L)).willReturn(entity);

		assertSame(entity, salesOrderService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(salesOrderRepository.count()).willReturn(42L);

		assertSame(42L, salesOrderService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(salesOrderRepository);
	}
}
