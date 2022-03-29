package dev.natanael.store.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;

public interface UserSessionService extends GenericService<UserSessionEntity, UUID> {

	public Page<UserSessionEntity> findByUser(UserEntity userEntity, Pageable pageable);

}