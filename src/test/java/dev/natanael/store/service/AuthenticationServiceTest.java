package dev.natanael.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import dev.natanael.store.StoreApplication;
import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;
import dev.natanael.store.repository.UserRepository;
import dev.natanael.store.repository.UserSessionRepository;
import dev.natanael.store.util.UserSessionContext;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class AuthenticationServiceTest {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserSessionContext userSessionContext;

	private List<UserSessionEntity> userSessionEntities = new ArrayList<UserSessionEntity>();
	private List<UserEntity> userEntities = new ArrayList<UserEntity>();

	@BeforeAll
	private void setUp() {
		UserEntity userEntity = new UserEntity();
		userEntity.setName("Test User 1");
		userEntity.setUsername("user1");
		userEntity.setPassword(passwordEncoder.encode("user1"));
		userEntity = userService.create(userEntity);
		userEntities.add(userEntity);

		userEntity = new UserEntity();
		userEntity.setName("Test User 2");
		userEntity.setUsername("user2");
		userEntity.setPassword(passwordEncoder.encode("user2"));
		userEntity = userService.create(userEntity);
		userEntities.add(userEntity);
	}

	@Test
	@Order(1)
	public void login_badCredentialsException() {
		assertThrows(BadCredentialsException.class, () -> {
			Authentication authentication = new UsernamePasswordAuthenticationToken("user1", "user2");
			authenticationService.login(authentication);
		});
	}

	@Test
	@Order(2)
	public void login() {
		Authentication authentication = new UsernamePasswordAuthenticationToken("user1", "user1");
		UserSessionEntity userSessionEntity = authenticationService.login(authentication);
		userSessionEntities.add(userSessionEntity);
		assertNotNull(userSessionEntity);
		assertNotNull(userSessionEntity.getId());
		assertTrue(userSessionContext.getUserSession().isPresent());
		assertEquals("user1", userSessionEntity.getUser().getUsername());
		assertEquals("user1", userSessionContext.getUserSession().get().getUser().getUsername());
	}

	@Test
	@Order(3)
	public void refresh() {
		UserSessionEntity userSessionEntity = authenticationService.refresh(userSessionEntities.get(0));
		assertNotNull(userSessionEntity);
		assertNotNull(userSessionEntity.getId());
		assertTrue(userSessionContext.getUserSession().isPresent());
		assertEquals("user1", userSessionEntity.getUser().getUsername());
		assertEquals("user1", userSessionContext.getUserSession().get().getUser().getUsername());
		assertNotEquals(userSessionEntities.get(0).getRefreshTokenSecret(), userSessionEntity.getRefreshTokenSecret());
	}

	@Test
	@Order(4)
	public void logout() {
		authenticationService.logout(userSessionEntities.get(0));
		assertTrue(userSessionRepository.findAll().isEmpty());
		assertTrue(userSessionContext.getUserSession().isEmpty());
 	}

	@AfterAll
	private void tearDown() {
		userSessionRepository.deleteAll();
		userRepository.deleteAll();
	}

}