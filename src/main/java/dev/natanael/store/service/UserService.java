package dev.natanael.store.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.natanael.store.model.entity.UserEntity;

public interface UserService extends GenericService<UserEntity, Long> {

	public Page<UserEntity> findByName(String name, Pageable pageable);
	
	public Optional<UserEntity> findByUsername(String username);

}