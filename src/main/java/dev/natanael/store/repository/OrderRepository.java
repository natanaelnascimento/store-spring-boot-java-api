package dev.natanael.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.model.entity.OrderEntity;
import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.model.entity.UserEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	public Page<OrderEntity> findByClient(ClientEntity client, Pageable pageable);

	public Page<OrderEntity> findByUser(UserEntity user, Pageable pageable);

	@Query("from OrderEntity o where exists (from o.items i where i.product = :product)")
	public Page<OrderEntity> findByProduct(@Param("product") ProductEntity product, Pageable pageable);

}
