package dev.natanael.store.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.natanael.store.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	public Page<UserEntity> findByNameIgnoreCaseContaining(String name, Pageable pageable);

	public Optional<UserEntity> findByUsernameIgnoreCase(String username);
	
}
