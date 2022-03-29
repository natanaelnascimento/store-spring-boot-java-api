
package dev.natanael.store.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.natanael.store.exception.CreditLimitOverflowException;
import dev.natanael.store.exception.InstallmentsLimitOverflowException;
import dev.natanael.store.exception.OutsideOfficeHoursException;
import dev.natanael.store.exception.OutsideWorkingDaysException;
import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.model.entity.DiscountEntity;
import dev.natanael.store.model.entity.OfficeHourEntity;
import dev.natanael.store.model.entity.OrderEntity;
import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;
import dev.natanael.store.repository.OrderRepository;
import dev.natanael.store.service.ClientService;
import dev.natanael.store.service.DiscountService;
import dev.natanael.store.service.OfficeHourService;
import dev.natanael.store.service.OrderService;
import dev.natanael.store.service.ProductService;
import dev.natanael.store.util.UserSessionContext;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private DiscountService discountService;

	@Autowired
	private OfficeHourService officeHourService;

	@Autowired
	private UserSessionContext userSessionContext;

	@Override
	public Optional<OrderEntity> findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public Page<OrderEntity> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public Iterable<OrderEntity> findAllById(Iterable<Long> ids) {
		return orderRepository.findAllById(ids);
	}

	@Override
	public OrderEntity create(OrderEntity order) {
		ClientEntity client = clientService
				.findById(order.getClient().getId())
				.orElseThrow(EntityNotFoundException::new);
		if(order.getInstallments() > client.getInstallmentsLimit())
			throw new InstallmentsLimitOverflowException();

		Optional<DiscountEntity> discountEntity = discountService.findByInstallments(order.getInstallments()); 
		
		updateItemPrice(order);
		BigDecimal subtotal = getSubtotal(order);
		BigDecimal discount = getDiscount(order, discountEntity);
		BigDecimal amount = subtotal.subtract(discount);
		if(amount.compareTo(client.getCreditLimit()) > 0)
			throw new CreditLimitOverflowException();
		order.setDiscount(discount);

		LocalDateTime orderDateTime = LocalDateTime.now();
		Page<OfficeHourEntity> officeHoursEntities = officeHourService.findByDayOfWeek(orderDateTime.getDayOfWeek().getValue(), Pageable.unpaged());
		officeHoursEntities.get().findAny().orElseThrow(OutsideWorkingDaysException::new);
		
		LocalTime orderTime = orderDateTime.toLocalTime();
		officeHoursEntities.get().filter(officeHour -> {
			return !orderTime.isBefore(officeHour.getStartTime()) && !orderTime.isAfter(officeHour.getEndTime());
		}).findAny().orElseThrow(OutsideOfficeHoursException::new);

		UserSessionEntity userSessionEntity = userSessionContext.getUserSession()
				.orElseThrow(EntityNotFoundException::new);

		order.setDateTime(orderDateTime);
		order.setUser(userSessionEntity.getUser());

		return orderRepository.save(order);
	}

	@Override
	public void update(OrderEntity order) {
		ClientEntity client = clientService
				.findById(order.getClient().getId())
				.orElseThrow(EntityNotFoundException::new);
		if(order.getInstallments() > client.getInstallmentsLimit())
			throw new InstallmentsLimitOverflowException();

		Optional<DiscountEntity> discountEntity = discountService.findByInstallments(order.getInstallments()); 

		updateItemPrice(order);
		BigDecimal subtotal = getSubtotal(order);
		BigDecimal discount = getDiscount(order, discountEntity);
		BigDecimal amount = subtotal.subtract(discount);
		if(amount.compareTo(client.getCreditLimit()) > 0)
			throw new CreditLimitOverflowException();
		order.setDiscount(discount);

		OrderEntity cleanItemsOrder = orderRepository.findById(order.getId())
				.orElseThrow(EntityNotFoundException::new);
		cleanItemsOrder.setItems(order.getItems());
		orderRepository.save(cleanItemsOrder);

		orderRepository.save(order);
	}

	@Override
	public void delete(OrderEntity entity) {
		orderRepository.delete(entity);
	}

	@Override
	public Page<OrderEntity> findByClient(ClientEntity client, Pageable pageable) {
		return orderRepository.findByClient(client, pageable);
	}

	@Override
	public Page<OrderEntity> findByUser(UserEntity user, Pageable pageable) {
		return orderRepository.findByUser(user, pageable);
	}

	@Override
	public Page<OrderEntity> findByProduct(ProductEntity product, Pageable pageable) {
		return orderRepository.findByProduct(product, pageable);
	}

	private void updateItemPrice(OrderEntity order) {
		List<Long> productIds = order.getItems().stream().map(p -> p.getProduct().getId()).toList();
		Iterable<ProductEntity> products = productService.findAllById(productIds);
		Map<Long, ProductEntity> mapProducts = new HashMap<>();
		products.forEach(p -> mapProducts.put(p.getId(), p));
		order.getItems().forEach(i -> {
			i.setProduct(mapProducts.get(i.getProduct().getId()));
			i.setPrice(mapProducts.get(i.getProduct().getId()).getPrice());
		});
	}

	private BigDecimal getSubtotal(OrderEntity order) {
		return order.getItems().stream()
			.map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal getDiscount(OrderEntity order, Optional<DiscountEntity> discount) {
		BigDecimal subtotal = getSubtotal(order);
		return discount.isPresent() ? subtotal.multiply(discount.get().getPercentage()) : BigDecimal.ZERO;
	}

}
