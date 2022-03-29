package dev.natanael.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes=StoreApplication.class)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeAll
	private void setUp() {
		UserEntity userEntity = new UserEntity();
		userEntity.setName("Test User 1");
		userEntity.setUsername("user1");
		userEntity.setPassword(passwordEncoder.encode("user1"));
		userEntity = userRepository.save(userEntity);

		userEntity = new UserEntity();
		userEntity.setName("Test User 2");
		userEntity.setUsername("user2");
		userEntity.setPassword(passwordEncoder.encode("user2"));
		userEntity = userRepository.save(userEntity);
	}

	@Test
	public void findByNameIgnoreCaseContaining() {
		Page<UserEntity> userEntities = userRepository.findByNameIgnoreCaseContaining("user 1", Pageable.unpaged());
		assertEquals(1, userEntities.getSize());
		assertEquals("Test User 1", userEntities.get().findFirst().get().getName());

		userEntities = userRepository.findByNameIgnoreCaseContaining("User 3", Pageable.unpaged());
		assertTrue(userEntities.isEmpty());
	}

	@Test
	public void findByUsernameIgnoreCase() {
		Optional<UserEntity> userEntity = userRepository.findByUsernameIgnoreCase("user2");
		assertTrue(userEntity.isPresent());
		assertEquals("Test User 2", userEntity.get().getName());

		userEntity = userRepository.findByUsernameIgnoreCase("user3");
		assertTrue(userEntity.isEmpty());
	}

	@AfterAll
	private void tearDown() {
		userRepository.deleteAll();
	}

}