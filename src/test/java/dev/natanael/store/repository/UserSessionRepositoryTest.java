package dev.natanael.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes=StoreApplication.class)
public class UserSessionRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private List<UserEntity> userEntities = new ArrayList<UserEntity>();

	@BeforeAll
	private void setUp() {
		UserEntity userEntity = new UserEntity();
		userEntity.setName("Test User 1");
		userEntity.setUsername("user1");
		userEntity.setPassword(passwordEncoder.encode("user1"));
		userEntity = userRepository.save(userEntity);
		userEntities.add(userEntity);

		UserSessionEntity userSessionEntity = new UserSessionEntity();
		userSessionEntity.setUser(userEntity);
		userSessionEntity.setAccessTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity.setRefreshTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity.setDateTime(LocalDateTime.now());
		userSessionEntity = userSessionRepository.save(userSessionEntity);

		userEntity = new UserEntity();
		userEntity.setName("Test User 2");
		userEntity.setUsername("user2");
		userEntity.setPassword(passwordEncoder.encode("user2"));
		userEntity = userRepository.save(userEntity);
		userEntities.add(userEntity);

		userSessionEntity = new UserSessionEntity();
		userSessionEntity.setUser(userEntity);
		userSessionEntity.setAccessTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity.setRefreshTokenSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
		userSessionEntity.setDateTime(LocalDateTime.now());
		userSessionEntity = userSessionRepository.save(userSessionEntity);
	}

	@Test
	public void findByUser() {
		Page<UserSessionEntity> userSessionEntities = userSessionRepository.findByUser(userEntities.get(0), Pageable.unpaged());
		assertEquals(1, userSessionEntities.getSize());
		assertEquals("Test User 1", userSessionEntities.get().findFirst().get().getUser().getName());
	}

	@AfterAll
	private void tearDown() {
		userSessionRepository.deleteAll();
		userRepository.deleteAll();
	}

}