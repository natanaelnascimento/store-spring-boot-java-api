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
import dev.natanael.store.model.entity.ClientEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes=StoreApplication.class)
public class ClientRepositoryTest {

	@Autowired
	private ClientRepository clientRepository;

	@BeforeAll
	private void setUp() {
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 1");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(500.0));
		clientEntity.setInstallmentsLimit(10);
		clientEntity = clientRepository.save(clientEntity);

		clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 2");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(1000.0));
		clientEntity.setInstallmentsLimit(12);
		clientEntity = clientRepository.save(clientEntity);
	}

	@Test
	public void findByNameIgnoreCaseContaining() {
		Page<ClientEntity> clientEntities = clientRepository.findByNameIgnoreCaseContaining("client 1", Pageable.unpaged());
		assertEquals(1, clientEntities.getSize());
		assertEquals("Test Client 1", clientEntities.get().findFirst().get().getName());

		clientEntities = clientRepository.findByNameIgnoreCaseContaining("Client 3", Pageable.unpaged());
		assertTrue(clientEntities.isEmpty());
	}

	@AfterAll
	private void tearDown() {
		clientRepository.deleteAll();
	}

}