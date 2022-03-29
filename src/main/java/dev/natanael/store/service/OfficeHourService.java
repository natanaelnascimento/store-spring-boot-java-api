package dev.natanael.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.natanael.store.model.entity.OfficeHourEntity;

public interface OfficeHourService extends GenericService<OfficeHourEntity, Long> {
	
	public Page<OfficeHourEntity> findByDayOfWeek(Integer dayOfWeek, Pageable pageable);

}