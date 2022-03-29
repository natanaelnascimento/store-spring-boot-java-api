package dev.natanael.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.natanael.store.model.entity.ProductEntity;

public interface ProductService extends GenericService<ProductEntity, Long> {
	
	public Page<ProductEntity> findByName(String name, Pageable pageable);

}