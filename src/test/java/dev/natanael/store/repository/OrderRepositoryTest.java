package dev.natanael.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.model.entity.OrderEntity;
import dev.natanael.store.model.entity.OrderItemEntity;
import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.model.entity.UserEntity;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes=StoreApplication.class)
public class OrderRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private List<ClientEntity> clientEntities = new ArrayList<>();
	private List<UserEntity> userEntities = new ArrayList<>();
	private List<ProductEntity> productEntities = new ArrayList<>();

	@BeforeAll
	private void setUp() {
		// Order 1
		List<OrderItemEntity> orderItemsEntities = new ArrayList<>();

		ProductEntity productEntity = new ProductEntity();
		productEntity.setName("Test Product 1");
		productEntity.setDescription("Test Product 1 description");
		productEntity.setPrice(BigDecimal.valueOf(500.0));
		productEntity = productRepository.save(productEntity);
		productEntities.add(productEntity);

		OrderItemEntity orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntity);
		orderItemEntity.setPrice(productEntity.getPrice());
		orderItemEntity.setQuantity(2);
		orderItemsEntities.add(orderItemEntity);

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 2");
		productEntity.setDescription("Test Product 2 description");
		productEntity.setPrice(BigDecimal.valueOf(1000.0));
		productEntity = productRepository.save(productEntity);
		productEntities.add(productEntity);

		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntity);
		orderItemEntity.setPrice(productEntity.getPrice());
		orderItemEntity.setQuantity(3);
		orderItemsEntities.add(orderItemEntity);

		UserEntity userEntity = new UserEntity();
		userEntity.setName("Test User 1");
		userEntity.setUsername("user1");
		userEntity.setPassword(passwordEncoder.encode("user1"));
		userEntity = userRepository.save(userEntity);
		userEntities.add(userEntity);
		
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 1");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(10000.0));
		clientEntity.setInstallmentsLimit(10);
		clientEntity = clientRepository.save(clientEntity);
		clientEntities.add(clientEntity);

		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setClient(clientEntity);
		orderEntity.setDateTime(LocalDateTime.now());
		orderEntity.setDiscount(BigDecimal.ZERO);
		orderEntity.setInstallments(5);
		orderEntity.setUser(userEntity);
		orderEntity.setItems(orderItemsEntities);
		orderEntity = orderRepository.save(orderEntity);
		
		// Order 2
		orderItemsEntities = new ArrayList<OrderItemEntity>();

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 3");
		productEntity.setDescription("Test Product 3 description");
		productEntity.setPrice(BigDecimal.valueOf(800.0));
		productEntity = productRepository.save(productEntity);
		productEntities.add(productEntity);

		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntity);
		orderItemEntity.setPrice(productEntity.getPrice());
		orderItemEntity.setQuantity(1);
		orderItemsEntities.add(orderItemEntity);
		
		productEntity = new ProductEntity();
		productEntity.setName("Test Product 4");
		productEntity.setDescription("Test Product 4 description");
		productEntity.setPrice(BigDecimal.valueOf(200.0));
		productEntity = productRepository.save(productEntity);
		productEntities.add(productEntity);

		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntity);
		orderItemEntity.setPrice(productEntity.getPrice());
		orderItemEntity.setQuantity(3);
		orderItemsEntities.add(orderItemEntity);

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 5");
		productEntity.setDescription("Test Product 5 description");
		productEntity.setPrice(BigDecimal.valueOf(450.0));
		productEntity = productRepository.save(productEntity);
		productEntities.add(productEntity);

		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntity);
		orderItemEntity.setPrice(productEntity.getPrice());
		orderItemEntity.setQuantity(2);
		orderItemsEntities.add(orderItemEntity);
		
		userEntity = new UserEntity();
		userEntity.setName("Test User 2");
		userEntity.setUsername("user2");
		userEntity.setPassword(passwordEncoder.encode("user2"));
		userEntity = userRepository.save(userEntity);
		userEntities.add(userEntity);

		clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 2");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(5000.0));
		clientEntity.setInstallmentsLimit(12);
		clientEntity = clientRepository.save(clientEntity);
		clientEntities.add(clientEntity);

		orderEntity = new OrderEntity();
		orderEntity.setClient(clientEntity);
		orderEntity.setDateTime(LocalDateTime.now());
		orderEntity.setDiscount(BigDecimal.ZERO);
		orderEntity.setInstallments(3);
		orderEntity.setUser(userEntity);
		orderEntity.setItems(orderItemsEntities);
		orderEntity = orderRepository.save(orderEntity);
	}

	@Test
	public void findByClient() {
		Page<OrderEntity> orderEntities = orderRepository.findByClient(clientEntities.get(0), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 1", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(2, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderRepository.findByClient(clientEntities.get(1), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 2", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(3, orderEntities.get().findFirst().get().getItems().size());
	}

	@Test
	public void findByUser() {
		Page<OrderEntity> orderEntities = orderRepository.findByUser(userEntities.get(0), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 1", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(2, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderRepository.findByUser(userEntities.get(1), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 2", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(3, orderEntities.get().findFirst().get().getItems().size());
	}

	@Test
	public void findByProduct() {
		Page<OrderEntity> orderEntities = orderRepository.findByProduct(productEntities.get(0), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 1", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(2, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderRepository.findByProduct(productEntities.get(1), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 1", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(2, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderRepository.findByProduct(productEntities.get(2), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 2", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(3, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderRepository.findByProduct(productEntities.get(3), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 2", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(3, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderRepository.findByProduct(productEntities.get(4), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 2", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(3, orderEntities.get().findFirst().get().getItems().size());
	}

	@AfterAll
	private void tearDown() {
		orderRepository.deleteAll();
		productRepository.deleteAll();
		clientRepository.deleteAll();
		userRepository.deleteAll();
	}

}