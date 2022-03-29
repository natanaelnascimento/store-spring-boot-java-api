package dev.natanael.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

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
import dev.natanael.store.model.entity.OfficeHourEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes=StoreApplication.class)
public class OfficeHourRepositoryTest {

	@Autowired
	private OfficeHourRepository officeHourRepository;

	@BeforeAll
	private void setUp() {
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(2);
		officeHourEntity.setStartTime(LocalTime.of(8, 0));
		officeHourEntity.setEndTime(LocalTime.of(18, 0));
		officeHourEntity = officeHourRepository.save(officeHourEntity);

		officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(3);
		officeHourEntity.setStartTime(LocalTime.of(10, 0));
		officeHourEntity.setEndTime(LocalTime.of(16, 0));
		officeHourEntity = officeHourRepository.save(officeHourEntity);
	}

	@Test
	public void findByDayOfWeek() {
		Page<OfficeHourEntity> officeHourEntities = officeHourRepository.findByDayOfWeek(2, Pageable.unpaged());
		assertEquals(1, officeHourEntities.getSize());
		assertEquals(LocalTime.of(8, 0), officeHourEntities.get().findFirst().get().getStartTime());
		assertEquals(LocalTime.of(18, 0), officeHourEntities.get().findFirst().get().getEndTime());

		officeHourEntities = officeHourRepository.findByDayOfWeek(3, Pageable.unpaged());
		assertEquals(1, officeHourEntities.getSize());
		assertEquals(LocalTime.of(10, 0), officeHourEntities.get().findFirst().get().getStartTime());
		assertEquals(LocalTime.of(16, 0), officeHourEntities.get().findFirst().get().getEndTime());

		officeHourEntities = officeHourRepository.findByDayOfWeek(4, Pageable.unpaged());
		assertTrue(officeHourEntities.isEmpty());
	}

	@AfterAll
	private void tearDown() {
		officeHourRepository.deleteAll();
	}

}