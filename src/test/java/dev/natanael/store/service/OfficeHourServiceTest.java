package dev.natanael.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
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
import dev.natanael.store.model.entity.OfficeHourEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class OfficeHourServiceTest {

	@Autowired
	private OfficeHourService officeHourService;

	private List<OfficeHourEntity> officeHourEntities = new ArrayList<OfficeHourEntity>();

	@Test
	@Order(1)
	public void create() {
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(2);
		officeHourEntity.setStartTime(LocalTime.of(8, 0));
		officeHourEntity.setEndTime(LocalTime.of(18, 0));
		officeHourEntity = officeHourService.create(officeHourEntity);
		officeHourEntities.add(officeHourEntity);
		assertNotNull(officeHourEntity);
		assertNotNull(officeHourEntity.getId());

		officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(3);
		officeHourEntity.setStartTime(LocalTime.of(10, 0));
		officeHourEntity.setEndTime(LocalTime.of(16, 0));
		officeHourEntity = officeHourService.create(officeHourEntity);
		officeHourEntities.add(officeHourEntity);
		assertNotNull(officeHourEntity);
		assertNotNull(officeHourEntity.getId());
	}

	@Test
	@Order(2)
	public void findAll() {
		Page<OfficeHourEntity> officeHourEntities = officeHourService.findAll(Pageable.unpaged());
		assertEquals(2, officeHourEntities.getSize());
	}

	@Test
	@Order(3)
	public void findById() {
		Optional<OfficeHourEntity> officeHourEntity = officeHourService.findById(officeHourEntities.get(0).getId());
		assertTrue(officeHourEntity.isPresent());
		assertEquals(LocalTime.of(8, 0), officeHourEntity.get().getStartTime());
		assertEquals(LocalTime.of(18, 0), officeHourEntity.get().getEndTime());

		officeHourEntity = officeHourService.findById(officeHourEntities.get(1).getId());
		assertTrue(officeHourEntity.isPresent());
		assertEquals(LocalTime.of(10, 0), officeHourEntity.get().getStartTime());
		assertEquals(LocalTime.of(16, 0), officeHourEntity.get().getEndTime());
	}

	@Test
	@Order(4)
	public void findAllById() {
		List<Long> ids = new ArrayList<>();
		ids.add(this.officeHourEntities.get(0).getId());
		Iterable<OfficeHourEntity> officeHourEntities = officeHourService.findAllById(ids);
		assertEquals(1, officeHourEntities.spliterator().getExactSizeIfKnown());

		ids.add(this.officeHourEntities.get(1).getId());
		officeHourEntities = officeHourService.findAllById(ids);
		assertEquals(2, officeHourEntities.spliterator().getExactSizeIfKnown());
	}

	@Test
	@Order(5)
	public void update() {
		OfficeHourEntity officeHourEntity = officeHourEntities.get(0);
		officeHourEntity.setEndTime(LocalTime.of(16, 0));
		officeHourService.update(officeHourEntity);
		Optional<OfficeHourEntity> officeHourEntityOptional = officeHourService.findById(officeHourEntity.getId());
		assertTrue(officeHourEntityOptional.isPresent());
		assertEquals(LocalTime.of(16, 0), officeHourEntityOptional.get().getEndTime());
		assertNotNull(officeHourEntity.getId());
	}

	@Test
	@Order(6)
	public void findByInstallments() {
		Page<OfficeHourEntity> officeHourEntities = officeHourService.findByDayOfWeek(2, Pageable.unpaged());
		assertEquals(1, officeHourEntities.getSize());
		assertEquals(LocalTime.of(8, 0), officeHourEntities.get().findFirst().get().getStartTime());
		assertEquals(LocalTime.of(16, 0), officeHourEntities.get().findFirst().get().getEndTime());

		officeHourEntities = officeHourService.findByDayOfWeek(3, Pageable.unpaged());
		assertEquals(1, officeHourEntities.getSize());
		assertEquals(LocalTime.of(10, 0), officeHourEntities.get().findFirst().get().getStartTime());
		assertEquals(LocalTime.of(16, 0), officeHourEntities.get().findFirst().get().getEndTime());

		officeHourEntities = officeHourService.findByDayOfWeek(4, Pageable.unpaged());
		assertTrue(officeHourEntities.isEmpty());
	}

	@Test
	@Order(7)
	public void delete() {
		OfficeHourEntity officeHourEntity = this.officeHourEntities.get(0);
		officeHourService.delete(officeHourEntity);
		Page<OfficeHourEntity> officeHourEntities = officeHourService.findAll(Pageable.unpaged());
		assertEquals(1, officeHourEntities.getSize());

		officeHourEntity = this.officeHourEntities.get(1);
		officeHourService.delete(officeHourEntity);
		officeHourEntities = officeHourService.findAll(Pageable.unpaged());
		assertEquals(0, officeHourEntities.getSize());
	}

}