package dev.natanael.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.natanael.store.model.entity.OfficeHourEntity;

@Repository
public interface OfficeHourRepository extends JpaRepository<OfficeHourEntity, Long> {

	public Page<OfficeHourEntity> findByDayOfWeek(Integer dayOfWeek, Pageable pageable);
	
}
