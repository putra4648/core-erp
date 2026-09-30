package id.putra.core_erp.modules.product.repository;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import id.putra.core_erp.modules.product.model.Product;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ProductRepositoryTest {
	@InjectMock
	ProductRepository productRepository;

	@Test
	public void findAllByName() throws Throwable{
		var p1 = new Product();
		p1.setId(1L);
		p1.setName("Product 1");

		var list = new ArrayList<Product>();
		list.add(p1);

		var page = Page.ofSize(10);
		var sort = Sort.by("name");

		Mockito.when(productRepository.find("sku like ?1", sort, "%").page(page).list()).thenReturn(list);
		Assertions.assertEquals(list, productRepository.find("sku like ?1", sort, "%").list());
		Mockito.verify(productRepository, Mockito.times(1)).find("sku like ?1", sort, "%");

	}

}
