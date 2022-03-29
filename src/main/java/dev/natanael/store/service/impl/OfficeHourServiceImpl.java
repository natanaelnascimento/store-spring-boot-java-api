package dev.natanael.store.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.natanael.store.model.entity.OfficeHourEntity;
import dev.natanael.store.repository.OfficeHourRepository;
import dev.natanael.store.service.OfficeHourService;

@Service
@Transactional
public class OfficeHourServiceImpl implements OfficeHourService {

	@Autowired
	private OfficeHourRepository officeHourRepository;

	@Override
	public Optional<OfficeHourEntity> findById(Long id) {
		return officeHourRepository.findById(id);
	}

	@Override
	public Page<OfficeHourEntity> findAll(Pageable pageable) {
		return officeHourRepository.findAll(pageable);
	}

	@Override
	public Iterable<OfficeHourEntity> findAllById(Iterable<Long> ids) {
		return officeHourRepository.findAllById(ids);
	}

	@Override
	public OfficeHourEntity create(OfficeHourEntity entity) {
		return officeHourRepository.save(entity);
	}

	@Override
	public void update(OfficeHourEntity entity) {
		officeHourRepository.save(entity);
	}

	@Override
	public void delete(OfficeHourEntity entity) {
		officeHourRepository.delete(entity);
	}

	@Override
	public Page<OfficeHourEntity> findByDayOfWeek(Integer dayOfWeek, Pageable pageable) {
		return officeHourRepository.findByDayOfWeek(dayOfWeek, pageable);
	}

}
