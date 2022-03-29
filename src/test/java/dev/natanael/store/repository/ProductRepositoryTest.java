package dev.natanael.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import dev.natanael.store.StoreApplication;
import dev.natanael.store.model.entity.ProductEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes=StoreApplication.class)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@BeforeAll
	private void setUp() {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setName("Test Product 1");
		productEntity.setDescription("Test Product 1 description");
		productEntity.setPrice(BigDecimal.valueOf(5000.0));
		productEntity = productRepository.save(productEntity);

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 2");
		productEntity.setDescription("Test Product 2 description");
		productEntity.setPrice(BigDecimal.valueOf(10000.0));
		productEntity = productRepository.save(productEntity);
	}

	@Test
	public void findByNameIgnoreCaseContaining() {
		Page<ProductEntity> productEntities = productRepository.findByNameIgnoreCaseContaining("product 1", Pageable.unpaged());
		assertEquals(1, productEntities.getSize());
		assertEquals("Test Product 1", productEntities.get().findFirst().get().getName());

		productEntities = productRepository.findByNameIgnoreCaseContaining("Product 3", Pageable.unpaged());
		assertTrue(productEntities.isEmpty());
	}

	@AfterAll
	private void tearDown() {
		productRepository.deleteAll();
	}

}