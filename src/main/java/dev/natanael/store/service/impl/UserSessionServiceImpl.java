package dev.natanael.store.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;
import dev.natanael.store.repository.UserSessionRepository;
import dev.natanael.store.service.UserSessionService;

@Service
@Transactional
public class UserSessionServiceImpl implements UserSessionService {

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Override
	public Optional<UserSessionEntity> findById(UUID id) {
		return userSessionRepository.findById(id);
	}

	@Override
	public Page<UserSessionEntity> findAll(Pageable pageable) {
		return userSessionRepository.findAll(pageable);
	}

	@Override
	public Iterable<UserSessionEntity> findAllById(Iterable<UUID> ids) {
		return userSessionRepository.findAllById(ids);
	}

	@Override
	public UserSessionEntity create(UserSessionEntity entity) {
		entity.setDateTime(LocalDateTime.now());
		return userSessionRepository.save(entity);
	}

	@Override
	public void update(UserSessionEntity entity) {
		userSessionRepository.save(entity);
	}

	@Override
	public void delete(UserSessionEntity entity) {
		userSessionRepository.delete(entity);
	}

	@Override
	public Page<UserSessionEntity> findByUser(UserEntity userEntity, Pageable pageable) {
		return userSessionRepository.findByUser(userEntity, pageable);
	}

}
