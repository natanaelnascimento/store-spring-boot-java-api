package dev.natanael.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import dev.natanael.store.StoreApplication;
import dev.natanael.store.exception.CreditLimitOverflowException;
import dev.natanael.store.exception.InstallmentsLimitOverflowException;
import dev.natanael.store.exception.OutsideOfficeHoursException;
import dev.natanael.store.exception.OutsideWorkingDaysException;
import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.model.entity.DiscountEntity;
import dev.natanael.store.model.entity.OfficeHourEntity;
import dev.natanael.store.model.entity.OrderEntity;
import dev.natanael.store.model.entity.OrderItemEntity;
import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.repository.ClientRepository;
import dev.natanael.store.repository.DiscountRepository;
import dev.natanael.store.repository.OfficeHourRepository;
import dev.natanael.store.repository.OrderRepository;
import dev.natanael.store.repository.ProductRepository;
import dev.natanael.store.repository.UserRepository;
import dev.natanael.store.repository.UserSessionRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes=StoreApplication.class)
public class OrderServiceTest {
	
	@Autowired private ProductRepository productRepository;
	@Autowired private ClientRepository clientRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private UserSessionRepository userSessionRepository;
	@Autowired private DiscountRepository discountRepository;
	@Autowired private OfficeHourRepository officeHourRepository;
	@Autowired private OrderRepository orderRepository;

	@Autowired private ProductService productService;
	@Autowired private ClientService clientService;
	@Autowired private UserService userService;
	@Autowired private AuthenticationService authenticationService;
	@Autowired private DiscountService discountService;
	@Autowired private OfficeHourService officeHourService;
	@Autowired private OrderService orderService;

	private List<OrderEntity> orderEntities = new ArrayList<>();
	private List<ClientEntity> clientEntities = new ArrayList<>();
	private List<ProductEntity> productEntities = new ArrayList<>();
	private List<UserEntity> userEntities = new ArrayList<>();

	@BeforeAll
	private void setUp() {
		// Users
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

		// Authentication
		authenticationService.login(new UsernamePasswordAuthenticationToken("user2", "user2"));

		// Discount
		DiscountEntity discountEntity = new DiscountEntity();
		discountEntity.setDescription("Test Discount 1");
		discountEntity.setInstallmentsLimit(1);
		discountEntity.setPercentage(BigDecimal.valueOf(0.1));
		discountService.create(discountEntity);

		// Clients
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 1");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(10000.0));
		clientEntity.setInstallmentsLimit(10);
		clientEntity = clientService.create(clientEntity);
		clientEntities.add(clientEntity);

		clientEntity = new ClientEntity();
		clientEntity.setName("Test Client 2");
		clientEntity.setAddress("Brazil");
		clientEntity.setCreditLimit(BigDecimal.valueOf(5000.0));
		clientEntity.setInstallmentsLimit(12);
		clientEntity = clientService.create(clientEntity);
		clientEntities.add(clientEntity);

		// Products
		ProductEntity productEntity = new ProductEntity();
		productEntity.setName("Test Product 1");
		productEntity.setDescription("Test Product 1 description");
		productEntity.setPrice(BigDecimal.valueOf(500.0));
		productEntity = productService.create(productEntity);
		productEntities.add(productEntity);

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 2");
		productEntity.setDescription("Test Product 2 description");
		productEntity.setPrice(BigDecimal.valueOf(1000.0));
		productEntity = productService.create(productEntity);
		productEntities.add(productEntity);
		
		productEntity = new ProductEntity();
		productEntity.setName("Test Product 3");
		productEntity.setDescription("Test Product 3 description");
		productEntity.setPrice(BigDecimal.valueOf(800.0));
		productEntity = productService.create(productEntity);
		productEntities.add(productEntity);

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 4");
		productEntity.setDescription("Test Product 4 description");
		productEntity.setPrice(BigDecimal.valueOf(200.0));
		productEntity = productService.create(productEntity);
		productEntities.add(productEntity);

		productEntity = new ProductEntity();
		productEntity.setName("Test Product 5");
		productEntity.setDescription("Test Product 5 description");
		productEntity.setPrice(BigDecimal.valueOf(450.0));
		productEntity = productService.create(productEntity);
		productEntities.add(productEntity);
	}

	@Test
	@Order(1)
	public void create_outsiteWorkingDaysException() {
		LocalDateTime orderDateTime = LocalDateTime.now();
		int dayOfWeek = orderDateTime.getDayOfWeek().plus(1).getValue();
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(dayOfWeek);
		officeHourEntity.setStartTime(LocalTime.of(0, 0));
		officeHourEntity.setEndTime(LocalTime.of(23, 59));
		officeHourService.create(officeHourEntity);
		
		assertThrows(OutsideWorkingDaysException.class, () -> {
			OrderEntity orderEntity = getOrder1(1, 1);
			orderEntity = orderService.create(orderEntity);
		});

		officeHourRepository.deleteAll();
	}

	@Test
	@Order(2)
	public void create_outsiteOfficeHoursException() {
		LocalDateTime orderDateTime = LocalDateTime.now();
		int dayOfWeek = orderDateTime.getDayOfWeek().getValue();
		int startTime = orderDateTime.getHour() < 12 ? 13 : 0;
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(dayOfWeek);
		officeHourEntity.setStartTime(LocalTime.of(startTime, 0));
		officeHourEntity.setEndTime(LocalTime.of(startTime + 8, 0));
		officeHourService.create(officeHourEntity);
		
		assertThrows(OutsideOfficeHoursException.class, () -> {
			OrderEntity orderEntity = getOrder1(1, 1);
			orderEntity = orderService.create(orderEntity);
		});

		officeHourRepository.deleteAll();
	}

	@Test
	@Order(3)
	public void create_creditLimitOverflowException() {
		LocalDateTime orderDateTime = LocalDateTime.now();
		int dayOfWeek = orderDateTime.getDayOfWeek().getValue();
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(dayOfWeek);
		officeHourEntity.setStartTime(LocalTime.of(0, 0));
		officeHourEntity.setEndTime(LocalTime.of(23, 59));
		officeHourService.create(officeHourEntity);

		assertThrows(CreditLimitOverflowException.class, () -> {
			OrderEntity orderEntity = getOrder1(1, 20);
			orderEntity = orderService.create(orderEntity);
		});

		officeHourRepository.deleteAll();
	}

	@Test
	@Order(4)
	public void create_installmentsLimitOverflowException() {
		LocalDateTime orderDateTime = LocalDateTime.now();
		int dayOfWeek = orderDateTime.getDayOfWeek().getValue();
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(dayOfWeek);
		officeHourEntity.setStartTime(LocalTime.of(0, 0));
		officeHourEntity.setEndTime(LocalTime.of(23, 59));
		officeHourService.create(officeHourEntity);

		assertThrows(InstallmentsLimitOverflowException.class, () -> {
			OrderEntity orderEntity = getOrder1(20, 2);
			orderEntity = orderService.create(orderEntity);
		});

		officeHourRepository.deleteAll();
	}

	@Test
	@Order(5)
	public void create() {
		LocalDateTime orderDateTime = LocalDateTime.now();
		int dayOfWeek = orderDateTime.getDayOfWeek().getValue();
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setDayOfWeek(dayOfWeek);
		officeHourEntity.setStartTime(LocalTime.of(0, 0));
		officeHourEntity.setEndTime(LocalTime.of(23, 59));
		officeHourService.create(officeHourEntity);

		OrderEntity orderEntity = getOrder1(1, 2);
		orderEntity = orderService.create(orderEntity);
		orderEntities.add(orderEntity);
		assertNotNull(orderEntity);
		assertNotNull(orderEntity.getId());

		orderEntity = getOrder2(1, 2);
		orderEntity = orderService.create(orderEntity);
		orderEntities.add(orderEntity);
		assertNotNull(orderEntity);
		assertNotNull(orderEntity.getId());
		assertEquals(290.0, orderEntity.getDiscount().doubleValue());
	}

	@Test
	@Order(6)
	public void findAll() {
		Page<OrderEntity> orderEntities = orderService.findAll(Pageable.unpaged());
		assertEquals(2, orderEntities.getSize());
	}

	@Test
	@Order(7)
	public void findById() {
		Optional<OrderEntity> orderEntity = orderService.findById(orderEntities.get(0).getId());
		assertTrue(orderEntity.isPresent());
		assertEquals("Test Client 1", orderEntity.get().getClient().getName());

		orderEntity = orderService.findById(orderEntities.get(1).getId());
		assertTrue(orderEntity.isPresent());
		assertEquals("Test Client 2", orderEntity.get().getClient().getName());
	}

	@Test
	@Order(8)
	public void findAllById() {
		List<Long> ids = new ArrayList<>();
		ids.add(this.orderEntities.get(0).getId());
		Iterable<OrderEntity> orderEntities = orderService.findAllById(ids);
		assertEquals(1, orderEntities.spliterator().getExactSizeIfKnown());

		ids.add(this.orderEntities.get(1).getId());
		orderEntities = orderService.findAllById(ids);
		assertEquals(2, orderEntities.spliterator().getExactSizeIfKnown());
	}

	@Test
	@Order(9)
	public void update() {
		List<OrderItemEntity> orderItemsEntities = new ArrayList<OrderItemEntity>();

		OrderItemEntity orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntities.get(2));
		orderItemEntity.setQuantity(1);
		orderItemsEntities.add(orderItemEntity);

		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntities.get(3));
		orderItemEntity.setQuantity(1);
		orderItemsEntities.add(orderItemEntity);

		OrderEntity orderEntity = orderEntities.get(0);
		orderEntity.getItems().addAll(orderItemsEntities);
		orderService.update(orderEntity);
		Optional<OrderEntity> orderEntityOptional = orderService.findById(orderEntity.getId());
		assertTrue(orderEntityOptional.isPresent());
		assertEquals(4, orderEntityOptional.get().getItems().size());
	}

	@Test
	@Order(10)
	public void findByClient() {
		Page<OrderEntity> orderEntities = orderService.findByClient(clientEntities.get(0), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 1", orderEntities.get().findFirst().get().getClient().getName());

		orderEntities = orderService.findByClient(clientEntities.get(1), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 2", orderEntities.get().findFirst().get().getClient().getName());
	}
	
	@Test
	@Order(11)
	public void findByUser() {
		Page<OrderEntity> orderEntities = orderService.findByUser(userEntities.get(0), Pageable.unpaged());
		assertTrue(orderEntities.isEmpty());

		orderEntities = orderService.findByUser(userEntities.get(1), Pageable.unpaged());
		assertEquals(2, orderEntities.getSize());
		assertEquals("Test User 2", orderEntities.get().findFirst().get().getUser().getName());
	}

	@Test
	@Order(12)
	public void findByProduct() {
		Page<OrderEntity> orderEntities = orderService.findByProduct(productEntities.get(0), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 1", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(4, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderService.findByProduct(productEntities.get(1), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 1", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(4, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderService.findByProduct(productEntities.get(2), Pageable.unpaged());
		assertEquals(2, orderEntities.getSize());
		assertEquals(4, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderService.findByProduct(productEntities.get(3), Pageable.unpaged());
		assertEquals(2, orderEntities.getSize());
		assertEquals(4, orderEntities.get().findFirst().get().getItems().size());

		orderEntities = orderService.findByProduct(productEntities.get(4), Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());
		assertEquals("Test Client 2", orderEntities.get().findFirst().get().getClient().getName());
		assertEquals(3, orderEntities.get().findFirst().get().getItems().size());
	}

	@Test
	@Order(13)
	public void delete() {
		OrderEntity orderEntity = this.orderEntities.get(0);
		orderService.delete(orderEntity);
		Page<OrderEntity> orderEntities = orderService.findAll(Pageable.unpaged());
		assertEquals(1, orderEntities.getSize());

		orderEntity = this.orderEntities.get(1);
		orderService.delete(orderEntity);
		orderEntities = orderService.findAll(Pageable.unpaged());
		assertEquals(0, orderEntities.getSize());
	}

	private OrderEntity getOrder1(int installments, int quantityPerItem) {
		List<OrderItemEntity> orderItemsEntities = new ArrayList<>();

		OrderItemEntity orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntities.get(0));
		orderItemEntity.setQuantity(quantityPerItem);
		orderItemsEntities.add(orderItemEntity);
		
		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntities.get(1));
		orderItemEntity.setQuantity(quantityPerItem);
		orderItemsEntities.add(orderItemEntity);
		
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setClient(clientEntities.get(0));
		orderEntity.setInstallments(installments);
		orderEntity.setItems(orderItemsEntities);
		orderEntity = orderService.create(orderEntity);

		return orderEntity;
	}

	private OrderEntity getOrder2(int installments, int quantityPerItem) {
		List<OrderItemEntity> orderItemsEntities = new ArrayList<OrderItemEntity>();

		OrderItemEntity orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntities.get(2));
		orderItemEntity.setQuantity(quantityPerItem);
		orderItemsEntities.add(orderItemEntity);

		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntities.get(3));
		orderItemEntity.setQuantity(quantityPerItem);
		orderItemsEntities.add(orderItemEntity);

		orderItemEntity = new OrderItemEntity();
		orderItemEntity.setProduct(productEntities.get(4));
		orderItemEntity.setQuantity(quantityPerItem);
		orderItemsEntities.add(orderItemEntity);

		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setClient(clientEntities.get(1));
		orderEntity.setInstallments(installments);
		orderEntity.setItems(orderItemsEntities);
		orderEntity = orderService.create(orderEntity);

		return orderEntity;
	}

	@AfterAll
	private void tearDown() {
		orderRepository.deleteAll();
		productRepository.deleteAll();
		clientRepository.deleteAll();
		discountRepository.deleteAll();
		userSessionRepository.deleteAll();
		userRepository.deleteAll();
		officeHourRepository.deleteAll();
	}

}