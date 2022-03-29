package dev.natanael.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.natanael.store.model.entity.DiscountEntity;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {

	@Query("from DiscountEntity d where d.installmentsLimit >= :installments"
			+ " and d.percentage = (select max(i.percentage) from DiscountEntity i"
			+ " where i.installmentsLimit >= :installments) and rownum = 1")
	public Optional<DiscountEntity> findByInstallments(@Param("installments") Integer installments);
	
}
