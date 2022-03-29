package dev.natanael.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class ProductServiceTest {

	@Autowired
	private ProductService productService;

	private List<ProductEntity> productEntities = new ArrayList<ProductEntity>();

	@Test
	@Order(1)
	public void create() {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setName("Test Product 1");
		productEntity.setDescription("Test Product 1 description");
		productEntity.setPrice(BigDecimal.valueOf(5000.0));
		productEntity = productService.create(productEntity);
		productEntities.add(productEntity);
		assertNotNull(productEntity);
		assertNotNull(productEntity.getId());

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 2");
		productEntity.setName("Test Product 2");
		productEntity.setDescription("Test Product 2 description");
		productEntity.setPrice(BigDecimal.valueOf(10000.0));
		productEntity = productService.create(productEntity);
		productEntities.add(productEntity);
		assertNotNull(productEntity);
		assertNotNull(productEntity.getId());
	}

	@Test
	@Order(2)
	public void findAll() {
		Page<ProductEntity> productEntities = productService.findAll(Pageable.unpaged());
		assertEquals(2, productEntities.getSize());
	}

	@Test
	@Order(3)
	public void findById() {
		Optional<ProductEntity> productEntity = productService.findById(productEntities.get(0).getId());
		assertTrue(productEntity.isPresent());
		assertEquals("Test Product 1", productEntity.get().getName());

		productEntity = productService.findById(productEntities.get(1).getId());
		assertTrue(productEntity.isPresent());
		assertEquals("Test Product 2", productEntity.get().getName());
	}

	@Test
	@Order(4)
	public void findAllById() {
		List<Long> ids = new ArrayList<>();
		ids.add(this.productEntities.get(0).getId());
		Iterable<ProductEntity> productEntities = productService.findAllById(ids);
		assertEquals(1, productEntities.spliterator().getExactSizeIfKnown());

		ids.add(this.productEntities.get(1).getId());
		productEntities = productService.findAllById(ids);
		assertEquals(2, productEntities.spliterator().getExactSizeIfKnown());
	}

	@Test
	@Order(5)
	public void update() {
		ProductEntity productEntity = productEntities.get(0);
		productEntity.setName("Test Product 3");
		productService.update(productEntity);
		Optional<ProductEntity> productEntityOptional = productService.findById(productEntity.getId());
		assertTrue(productEntityOptional.isPresent());
		assertEquals("Test Product 3", productEntityOptional.get().getName());
		assertNotNull(productEntity.getId());
	}

	@Test
	@Order(6)
	public void findByName() {
		Page<ProductEntity> productEntities = productService.findByName("product 2", Pageable.unpaged());
		assertEquals(1, productEntities.getSize());
		assertEquals("Test Product 2", productEntities.get().findFirst().get().getName());

		productEntities = productService.findByName("Product 1", Pageable.unpaged());
		assertTrue(productEntities.isEmpty());
	}

	@Test
	@Order(7)
	public void delete() {
		ProductEntity productEntity = this.productEntities.get(0);
		productService.delete(productEntity);
		Page<ProductEntity> productEntities = productService.findAll(Pageable.unpaged());
		assertEquals(1, productEntities.getSize());

		productEntity = this.productEntities.get(1);
		productService.delete(productEntity);
		productEntities = productService.findAll(Pageable.unpaged());
		assertEquals(0, productEntities.getSize());
	}

}