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
import dev.natanael.store.model.entity.DiscountEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class DiscountServiceTest {

	@Autowired
	private DiscountService discountService;

	private List<DiscountEntity> discountEntities = new ArrayList<DiscountEntity>();

	@Test
	@Order(1)
	public void create() {
		DiscountEntity discountEntity = new DiscountEntity();
		discountEntity.setDescription("Test Discount 1");
		discountEntity.setInstallmentsLimit(2);
		discountEntity.setPercentage(BigDecimal.valueOf(0.1));
		discountEntity = discountService.create(discountEntity);
		discountEntities.add(discountEntity);
		assertNotNull(discountEntity);
		assertNotNull(discountEntity.getId());

		discountEntity = new DiscountEntity();
		discountEntity.setDescription("Test Discount 2");
		discountEntity.setInstallmentsLimit(1);
		discountEntity.setPercentage(BigDecimal.valueOf(0.15));
		discountEntity = discountService.create(discountEntity);
		discountEntities.add(discountEntity);
		assertNotNull(discountEntity);
		assertNotNull(discountEntity.getId());
	}

	@Test
	@Order(2)
	public void findAll() {
		Page<DiscountEntity> discountEntities = discountService.findAll(Pageable.unpaged());
		assertEquals(2, discountEntities.getSize());
	}

	@Test
	@Order(3)
	public void findById() {
		Optional<DiscountEntity> discountEntity = discountService.findById(discountEntities.get(0).getId());
		assertTrue(discountEntity.isPresent());
		assertEquals("Test Discount 1", discountEntity.get().getDescription());

		discountEntity = discountService.findById(discountEntities.get(1).getId());
		assertTrue(discountEntity.isPresent());
		assertEquals("Test Discount 2", discountEntity.get().getDescription());
	}

	@Test
	@Order(4)
	public void findAllById() {
		List<Long> ids = new ArrayList<>();
		ids.add(this.discountEntities.get(0).getId());
		Iterable<DiscountEntity> discountEntities = discountService.findAllById(ids);
		assertEquals(1, discountEntities.spliterator().getExactSizeIfKnown());

		ids.add(this.discountEntities.get(1).getId());
		discountEntities = discountService.findAllById(ids);
		assertEquals(2, discountEntities.spliterator().getExactSizeIfKnown());
	}

	@Test
	@Order(5)
	public void update() {
		DiscountEntity discountEntity = discountEntities.get(0);
		discountEntity.setDescription("Test Discount 3");
		discountService.update(discountEntity);
		Optional<DiscountEntity> discountEntityOptional = discountService.findById(discountEntity.getId());
		assertTrue(discountEntityOptional.isPresent());
		assertEquals("Test Discount 3", discountEntityOptional.get().getDescription());
		assertNotNull(discountEntity.getId());
	}

	@Test
	@Order(6)
	public void findByInstallments() {
		Optional<DiscountEntity> discountEntity = discountService.findByInstallments(1);
		assertTrue(discountEntity.isPresent());
		assertEquals("Test Discount 2", discountEntity.get().getDescription());
		assertEquals(0.15, discountEntity.get().getPercentage().doubleValue());

		discountEntity = discountService.findByInstallments(2);
		assertTrue(discountEntity.isPresent());
		assertEquals("Test Discount 3", discountEntity.get().getDescription());
		assertEquals(0.1, discountEntity.get().getPercentage().doubleValue());

		discountEntity = discountService.findByInstallments(3);
		assertTrue(discountEntity.isEmpty());
	}

	@Test
	@Order(7)
	public void delete() {
		DiscountEntity discountEntity = this.discountEntities.get(0);
		discountService.delete(discountEntity);
		Page<DiscountEntity> discountEntities = discountService.findAll(Pageable.unpaged());
		assertEquals(1, discountEntities.getSize());

		discountEntity = this.discountEntities.get(1);
		discountService.delete(discountEntity);
		discountEntities = discountService.findAll(Pageable.unpaged());
		assertEquals(0, discountEntities.getSize());
	}

}