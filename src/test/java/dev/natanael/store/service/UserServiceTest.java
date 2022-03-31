package dev.natanael.store.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import dev.natanael.store.StoreApplication;
import dev.natanael.store.exception.IncorrectPasswordException;
import dev.natanael.store.exception.SessionUserDeleteException;
import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.repository.UserRepository;
import dev.natanael.store.repository.UserSessionRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class UserServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;

	private List<UserEntity> userEntities = new ArrayList<UserEntity>();

	@Test
	@Order(1)
	public void create() {
		UserEntity userEntity = new UserEntity();
		userEntity.setName("Test User 1");
		userEntity.setUsername("user1");
		userEntity.setPassword("user1");
		userEntity = userService.create(userEntity);
		userEntities.add(userEntity);
		assertNotNull(userEntity);
		assertNotNull(userEntity.getId());

		userEntity = new UserEntity();
		userEntity.setName("Test User 2");
		userEntity.setUsername("user2");
		userEntity.setPassword("user2");
		userEntity = userService.create(userEntity);
		userEntities.add(userEntity);
		assertNotNull(userEntity);
		assertNotNull(userEntity.getId());

		// Authentication
		authenticationService.login(new UsernamePasswordAuthenticationToken("user1", "user1"));
	}

	@Test
	@Order(2)
	public void findAll() {
		Page<UserEntity> userEntities = userService.findAll(Pageable.unpaged());
		assertEquals(2, userEntities.getSize());
	}

	@Test
	@Order(3)
	public void findById() {
		Optional<UserEntity> userEntity = userService.findById(userEntities.get(0).getId());
		assertTrue(userEntity.isPresent());
		assertEquals("Test User 1", userEntity.get().getName());

		userEntity = userService.findById(userEntities.get(1).getId());
		assertTrue(userEntity.isPresent());
		assertEquals("Test User 2", userEntity.get().getName());
	}

	@Test
	@Order(4)
	public void findAllById() {
		List<Long> ids = new ArrayList<>();
		ids.add(this.userEntities.get(0).getId());
		Iterable<UserEntity> userEntities = userService.findAllById(ids);
		assertEquals(1, userEntities.spliterator().getExactSizeIfKnown());

		ids.add(this.userEntities.get(1).getId());
		userEntities = userService.findAllById(ids);
		assertEquals(2, userEntities.spliterator().getExactSizeIfKnown());
	}

	@Test
	@Order(5)
	public void update() {
		UserEntity userEntity = userEntities.get(0);
		userEntity.setName("Test User 3");
		userService.update(userEntity);
		Optional<UserEntity> userEntityOptional = userService.findById(userEntity.getId());
		assertTrue(userEntityOptional.isPresent());
		assertEquals("Test User 3", userEntityOptional.get().getName());
	}

	@Test
	@Order(6)
	public void findByName() {
		Page<UserEntity> userEntities = userService.findByName("user 2", Pageable.unpaged());
		assertEquals(1, userEntities.getSize());
		assertEquals("Test User 2", userEntities.get().findFirst().get().getName());

		userEntities = userService.findByName("User 1", Pageable.unpaged());
		assertTrue(userEntities.isEmpty());
	}

	@Test
	@Order(7)
	public void findByUsername() {
		Optional<UserEntity> userEntity = userService.findByUsername("user1");
		assertTrue(userEntity.isPresent());
		assertEquals("Test User 3", userEntity.get().getName());

		userEntity = userService.findByUsername("user2");
		assertTrue(userEntity.isPresent());
		assertEquals("Test User 2", userEntity.get().getName());
	}

	@Test
	@Order(8)
	public void changePassword_incorrectPasswordException() {
		assertThrows(IncorrectPasswordException.class, () -> {
			userService.changePassword("user3", "user2");
		});
	}

	@Test
	@Order(9)
	public void changePassword() {
		userService.changePassword("user1", "user2");
		assertDoesNotThrow(() -> {
			authenticationService.login(new UsernamePasswordAuthenticationToken("user1", "user2"));
		});
	}

	@Test
	@Order(10)
	public void delete_sessionUserDeleteException() {
		UserEntity userEntity = this.userEntities.get(0);
		assertThrows(SessionUserDeleteException.class, () ->{
			userService.delete(userEntity);
		});
	}

	@Test
	@Order(11)
	public void delete() {
		UserEntity userEntity = this.userEntities.get(1);
		userService.delete(userEntity);
		Page<UserEntity> userEntities = userService.findAll(Pageable.unpaged());
		assertEquals(1, userEntities.getSize());
	}

	@AfterAll
	private void tearDown() {
		userSessionRepository.deleteAll();
		userRepository.deleteAll();
	}

}