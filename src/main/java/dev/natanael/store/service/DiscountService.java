package dev.natanael.store.service;

import java.util.Optional;

import dev.natanael.store.model.entity.DiscountEntity;

public interface DiscountService extends GenericService<DiscountEntity, Long> {
	
	public Optional<DiscountEntity> findByInstallments(Integer installments);

}