package dev.natanael.store.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.natanael.store.model.entity.DiscountEntity;
import dev.natanael.store.repository.DiscountRepository;
import dev.natanael.store.service.DiscountService;

@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

	@Autowired
	private DiscountRepository discountRepository;

	@Override
	public Optional<DiscountEntity> findById(Long id) {
		return discountRepository.findById(id);
	}

	@Override
	public Page<DiscountEntity> findAll(Pageable pageable) {
		return discountRepository.findAll(pageable);
	}

	@Override
	public Iterable<DiscountEntity> findAllById(Iterable<Long> ids) {
		return discountRepository.findAllById(ids);
	}

	@Override
	public DiscountEntity create(DiscountEntity entity) {
		return discountRepository.save(entity);
	}

	@Override
	public void update(DiscountEntity entity) {
		discountRepository.save(entity);
	}

	@Override
	public void delete(DiscountEntity entity) {
		discountRepository.delete(entity);
	}

	@Override
	public Optional<DiscountEntity> findByInstallments(Integer installments) {
		return discountRepository.findByInstallments(installments);
	}

}
