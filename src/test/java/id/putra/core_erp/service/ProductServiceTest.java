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

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.repository.ProductRepository;
import id.putra.core_erp.modules.product.service.ProductService;
import id.putra.core_erp.support.ServiceDelegationAssertions;
import id.putra.core_erp.support.TestFixtures;

/**
 * Plain JUnit + Mockito: {@link ProductService} holds no business rules, so
 * there is no
 * CDI, transaction or database involved. Keeping it out of {@code @QuarkusTest}
 * means it
 * runs in milliseconds and does not need the dev-services container.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	ProductRepository productRepository;

	@InjectMocks
	ProductService productService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Product p = TestFixtures.product("SKU-1");

		productService.save(p);

		verify(productRepository).persist(p);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		productService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(productRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Product p = TestFixtures.product("SKU-2");
		given(productRepository.findById(3L)).willReturn(p);

		assertSame(p, productService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(productRepository.count()).willReturn(42L);

		assertSame(42L, productService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(productRepository);
	}
}
