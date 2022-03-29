package dev.natanael.store.service;

import org.springframework.security.core.Authentication;

import dev.natanael.store.model.entity.UserSessionEntity;

public interface AuthenticationService {

	public UserSessionEntity login(Authentication authentication);

	public UserSessionEntity refresh(UserSessionEntity userSessionEntity);

	public void logout(UserSessionEntity userSessionEntity);

}