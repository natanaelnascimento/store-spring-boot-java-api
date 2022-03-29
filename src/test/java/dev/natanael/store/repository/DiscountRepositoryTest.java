package dev.natanael.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import dev.natanael.store.StoreApplication;
import dev.natanael.store.model.entity.DiscountEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes=StoreApplication.class)
public class DiscountRepositoryTest {

	@Autowired
	private DiscountRepository discountRepository;

	@BeforeAll
	private void setUp() {
		DiscountEntity discountEntity = new DiscountEntity();
		discountEntity.setDescription("Test Discount 1");
		discountEntity.setInstallmentsLimit(2);
		discountEntity.setPercentage(BigDecimal.valueOf(0.1));
		discountEntity = discountRepository.save(discountEntity);

		discountEntity = new DiscountEntity();
		discountEntity.setDescription("Test Discount 2");
		discountEntity.setInstallmentsLimit(1);
		discountEntity.setPercentage(BigDecimal.valueOf(0.15));
		discountEntity = discountRepository.save(discountEntity);
	}

	@Test
	public void findByInstallments() {
		Optional<DiscountEntity> discountEntity = discountRepository.findByInstallments(1);
		assertTrue(discountEntity.isPresent());
		assertEquals("Test Discount 2", discountEntity.get().getDescription());
		assertEquals(0.15, discountEntity.get().getPercentage().doubleValue());

		discountEntity = discountRepository.findByInstallments(2);
		assertTrue(discountEntity.isPresent());
		assertEquals("Test Discount 1", discountEntity.get().getDescription());
		assertEquals(0.1, discountEntity.get().getPercentage().doubleValue());

		discountEntity = discountRepository.findByInstallments(3);
		assertTrue(discountEntity.isEmpty());
	}

	@AfterAll
	private void tearDown() {
		discountRepository.deleteAll();
	}

}