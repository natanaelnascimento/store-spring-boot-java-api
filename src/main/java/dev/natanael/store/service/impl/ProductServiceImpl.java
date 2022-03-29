package dev.natanael.store.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.repository.ProductRepository;
import dev.natanael.store.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Optional<ProductEntity> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public Page<ProductEntity> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	public Iterable<ProductEntity> findAllById(Iterable<Long> ids) {
		return productRepository.findAllById(ids);
	}

	@Override
	public ProductEntity create(ProductEntity entity) {
		return productRepository.save(entity);
	}

	@Override
	public void update(ProductEntity entity) {
		productRepository.save(entity);
	}

	@Override
	public void delete(ProductEntity entity) {
		productRepository.delete(entity);
	}

	@Override
	public Page<ProductEntity> findByName(String name, Pageable pageable) {
		return productRepository.findByNameIgnoreCaseContaining(name, pageable);
	}

}
