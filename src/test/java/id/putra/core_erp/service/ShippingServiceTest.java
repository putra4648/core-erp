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

import id.putra.core_erp.modules.outbound.model.Shipment;
import id.putra.core_erp.modules.outbound.repository.ShipmentRepository;
import id.putra.core_erp.modules.outbound.service.ShippingService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link ShippingService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class ShippingServiceTest {

	@Mock
	ShipmentRepository shipmentRepository;

	@InjectMocks
	ShippingService shippingService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Shipment entity = new Shipment();

		shippingService.save(entity);

		verify(shipmentRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		shippingService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(shipmentRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Shipment entity = new Shipment();
		given(shipmentRepository.findById(3L)).willReturn(entity);

		assertSame(entity, shippingService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(shipmentRepository.count()).willReturn(42L);

		assertSame(42L, shippingService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(shipmentRepository);
	}
}
