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
import dev.natanael.store.model.entity.ClientEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class ClientServiceTest {

	@Autowired
	private ClientService clientService;

	private List<ClientEntity> clientEntities = new ArrayList<ClientEntity>();

	@Test
	@Order(1)
	public void create() {
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 1");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(500.0));
		clientEntity.setInstallmentsLimit(10);
		clientEntity = clientService.create(clientEntity);
		clientEntities.add(clientEntity);
		assertNotNull(clientEntity);
		assertNotNull(clientEntity.getId());

		clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 2");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(1000.0));
		clientEntity.setInstallmentsLimit(12);
		clientEntity = clientService.create(clientEntity);
		clientEntities.add(clientEntity);
		assertNotNull(clientEntity);
		assertNotNull(clientEntity.getId());
	}

	@Test
	@Order(2)
	public void findAll() {
		Page<ClientEntity> clientEntities = clientService.findAll(Pageable.unpaged());
		assertEquals(2, clientEntities.getSize());
	}

	@Test
	@Order(3)
	public void findById() {
		Optional<ClientEntity> clientEntity = clientService.findById(clientEntities.get(0).getId());
		assertTrue(clientEntity.isPresent());
		assertEquals("Test Client 1", clientEntity.get().getName());

		clientEntity = clientService.findById(clientEntities.get(1).getId());
		assertTrue(clientEntity.isPresent());
		assertEquals("Test Client 2", clientEntity.get().getName());
	}

	@Test
	@Order(4)
	public void findAllById() {
		List<Long> ids = new ArrayList<>();
		ids.add(this.clientEntities.get(0).getId());
		Iterable<ClientEntity> clientEntities = clientService.findAllById(ids);
		assertEquals(1, clientEntities.spliterator().getExactSizeIfKnown());

		ids.add(this.clientEntities.get(1).getId());
		clientEntities = clientService.findAllById(ids);
		assertEquals(2, clientEntities.spliterator().getExactSizeIfKnown());
	}

	@Test
	@Order(5)
	public void update() {
		ClientEntity clientEntity = clientEntities.get(0);
		clientEntity.setName("Test Client 3");
		clientService.update(clientEntity);
		Optional<ClientEntity> clientEntityOptional = clientService.findById(clientEntity.getId());
		assertTrue(clientEntityOptional.isPresent());
		assertEquals("Test Client 3", clientEntityOptional.get().getName());
		assertNotNull(clientEntity.getId());
	}

	@Test
	@Order(6)
	public void findByName() {
		Page<ClientEntity> clientEntities = clientService.findByName("client 2", Pageable.unpaged());
		assertEquals(1, clientEntities.getSize());
		assertEquals("Test Client 2", clientEntities.get().findFirst().get().getName());

		clientEntities = clientService.findByName("Client 1", Pageable.unpaged());
		assertTrue(clientEntities.isEmpty());
	}

	@Test
	@Order(7)
	public void delete() {
		ClientEntity clientEntity = this.clientEntities.get(0);
		clientService.delete(clientEntity);
		Page<ClientEntity> clientEntities = clientService.findAll(Pageable.unpaged());
		assertEquals(1, clientEntities.getSize());

		clientEntity = this.clientEntities.get(1);
		clientService.delete(clientEntity);
		clientEntities = clientService.findAll(Pageable.unpaged());
		assertEquals(0, clientEntities.getSize());
	}

}