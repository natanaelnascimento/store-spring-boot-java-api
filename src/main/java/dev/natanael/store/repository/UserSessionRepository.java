package dev.natanael.store.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessionEntity, UUID> {

	public Page<UserSessionEntity> findByUser(UserEntity user, Pageable pageable);

}
