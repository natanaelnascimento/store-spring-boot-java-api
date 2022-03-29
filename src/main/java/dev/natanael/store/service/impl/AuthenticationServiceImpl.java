package dev.natanael.store.service.impl;

import java.util.Collections;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;
import dev.natanael.store.service.AuthenticationService;
import dev.natanael.store.service.UserService;
import dev.natanael.store.service.UserSessionService;
import dev.natanael.store.util.UserSessionContext;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSessionService userSessionService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserSessionContext userSessionContext;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserSessionEntity login(Authentication authentication) {
		authentication = authenticationManager.authenticate(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		UserEntity userEntity = userService.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		UserSessionEntity userSessionEntity = new UserSessionEntity();
		userSessionEntity.setUser(userEntity);
		userSessionEntity.setAccessTokenSecret(generateSecret());
		userSessionEntity.setRefreshTokenSecret(generateSecret());
		userSessionEntity = userSessionService.create(userSessionEntity);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		userSessionContext.setUserSession(userSessionEntity);

		return userSessionEntity;
	}

	@Override
	public UserSessionEntity refresh(UserSessionEntity userSessionEntity) {
		userSessionEntity = userSessionService
				.findById(userSessionEntity.getId())
				.orElseThrow(() -> new InsufficientAuthenticationException("User session not found"));
		userSessionEntity.setRefreshTokenSecret(generateSecret());
		userSessionService.update(userSessionEntity);

		UserEntity userEntity = userSessionEntity.getUser();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity, null, Collections.emptyList());
	
		SecurityContextHolder.getContext().setAuthentication(authentication);
		userSessionContext.setUserSession(userSessionEntity);

		return userSessionEntity;
	}

	@Override
	public void logout(UserSessionEntity userSessionEntity) {
		userSessionService.delete(userSessionEntity);
		SecurityContextHolder.getContext().setAuthentication(null);
		userSessionContext.setUserSession(null);
	}

	private String generateSecret() {
		return passwordEncoder.encode(UUID.randomUUID().toString());
	}

}
