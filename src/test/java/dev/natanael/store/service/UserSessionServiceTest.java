package dev.natanael.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import dev.natanael.store.StoreApplication;
import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;
import dev.natanael.store.repository.UserRepository;
import dev.natanael.store.repository.UserSessionRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class UserSessionServiceTest {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSessionService userSessionService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private List<UserSessionEntity> userSessionEntities = new ArrayList<UserSessionEntity>();
	private List<UserEntity> userEntities = new ArrayList<UserEntity>();

	@BeforeAll
	private void setUp() {
		UserEntity userEntity = new UserEntity();
		userEntity.setName("Test User 1");
		userEntity.setUsername("user1");
		userEntity.setPassword("user1");
		userEntity = userService.create(userEntity);
		userEntities.add(userEntity);

		userEntity = new UserEntity();
		userEntity.setName("Test User 2");
		userEntity.setUsername("user2");
		userEntity.setPassword("user2");
		userEntity = userService.create(userEntity);
		userEntities.add(userEntity);
	}

	@Test
	@Order(1)
	public void create() {
		UserSessionEntity userSessionEntity = new UserSessionEntity();
		userSessionEntity.setUser(userEntities.get(0));
		userSessionEntity.setAccessTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity.setRefreshTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity = userSessionService.create(userSessionEntity);
		userSessionEntities.add(userSessionEntity);
		assertNotNull(userSessionEntity);
		assertNotNull(userSessionEntity.getId());

		userSessionEntity = new UserSessionEntity();
		userSessionEntity.setUser(userEntities.get(1));
		userSessionEntity.setAccessTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity.setRefreshTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity = userSessionService.create(userSessionEntity);
		userSessionEntities.add(userSessionEntity);
		assertNotNull(userSessionEntity);
		assertNotNull(userSessionEntity.getId());
	}

	@Test
	@Order(2)
	public void findAll() {
		Page<UserSessionEntity> userSessionEntities = userSessionService.findAll(Pageable.unpaged());
		assertEquals(2, userSessionEntities.getSize());
	}

	@Test
	@Order(3)
	public void findById() {
		Optional<UserSessionEntity> userSessionEntity = userSessionService.findById(userSessionEntities.get(0).getId());
		assertTrue(userSessionEntity.isPresent());
		assertEquals("Test User 1", userSessionEntity.get().getUser().getName());

		userSessionEntity = userSessionService.findById(userSessionEntities.get(1).getId());
		assertTrue(userSessionEntity.isPresent());
		assertEquals("Test User 2", userSessionEntity.get().getUser().getName());
	}

	@Test
	@Order(4)
	public void findAllById() {
		List<UUID> ids = new ArrayList<>();
		ids.add(this.userSessionEntities.get(0).getId());
		Iterable<UserSessionEntity> userSessionEntities = userSessionService.findAllById(ids);
		assertEquals(1, userSessionEntities.spliterator().getExactSizeIfKnown());

		ids.add(this.userSessionEntities.get(1).getId());
		userSessionEntities = userSessionService.findAllById(ids);
		assertEquals(2, userSessionEntities.spliterator().getExactSizeIfKnown());
	}

	@Test
	@Order(5)
	public void update() {
		String refreshTokenSecret = UUID.randomUUID().toString();
		UserSessionEntity userSessionEntity = userSessionEntities.get(0);
		userSessionEntity.setRefreshTokenSecret(refreshTokenSecret);
		userSessionService.update(userSessionEntity);
		Optional<UserSessionEntity> userSessionEntityOptional = userSessionService.findById(userSessionEntity.getId());
		assertTrue(userSessionEntityOptional.isPresent());
		assertEquals(refreshTokenSecret, userSessionEntityOptional.get().getRefreshTokenSecret());
	}

	@Test
	@Order(6)
	public void findByUser() {
		Page<UserSessionEntity> userSessionEntities = userSessionService.findByUser(userEntities.get(0), Pageable.unpaged());
		assertEquals(1, userSessionEntities.getSize());
		assertEquals("Test User 1", userSessionEntities.get().findFirst().get().getUser().getName());

		userSessionEntities = userSessionService.findByUser(userEntities.get(1), Pageable.unpaged());
		assertEquals(1, userSessionEntities.getSize());
		assertEquals("Test User 2", userSessionEntities.get().findFirst().get().getUser().getName());
	}

	@Test
	@Order(7)
	public void delete() {
		UserSessionEntity userSessionEntity = this.userSessionEntities.get(0);
		userSessionService.delete(userSessionEntity);
		Page<UserSessionEntity> userSessionEntities = userSessionService.findAll(Pageable.unpaged());
		assertEquals(1, userSessionEntities.getSize());

		userSessionEntity = this.userSessionEntities.get(1);
		userSessionService.delete(userSessionEntity);
		userSessionEntities = userSessionService.findAll(Pageable.unpaged());
		assertEquals(0, userSessionEntities.getSize());
	}

	@AfterAll
	private void tearDown() {
		userSessionRepository.deleteAll();
		userRepository.deleteAll();
	}

}