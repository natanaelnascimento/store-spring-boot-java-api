package dev.natanael.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.model.entity.OrderEntity;
import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.model.entity.UserEntity;

public interface OrderService extends GenericService<OrderEntity, Long> {
	
	public Page<OrderEntity> findByClient(ClientEntity user, Pageable pageable);

	public Page<OrderEntity> findByUser(UserEntity user, Pageable pageable);

	public Page<OrderEntity> findByProduct(ProductEntity product, Pageable pageable);

}