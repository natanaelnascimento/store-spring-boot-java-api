package dev.natanael.store.controller.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.natanael.store.model.dto.LoginRequestDTO;
import dev.natanael.store.model.dto.LoginResponseDTO;
import dev.natanael.store.model.dto.RefreshRequestDTO;
import dev.natanael.store.model.entity.UserSessionEntity;
import dev.natanael.store.service.AuthenticationService;
import dev.natanael.store.util.JwtAuthenticationUtil;
import dev.natanael.store.util.UserSessionContext;

@RestController
@CrossOrigin
@RequestMapping("/v1/authentication")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private JwtAuthenticationUtil jwtAuthenticationUtil;
	
	@Autowired
	private UserSessionContext userSessionContext;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
		UserSessionEntity userSessionEntity = authenticationService.login(authentication);
		String accessToken = jwtAuthenticationUtil.generateAccessToken(userSessionEntity);
		String refreshToken = jwtAuthenticationUtil.generateRefreshToken(userSessionEntity);
		return ResponseEntity.ok(
				new LoginResponseDTO(accessToken, refreshToken, jwtAuthenticationUtil.getAccessTokenExpirationTime()));
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO refreshRequestDTO) {
		UserSessionEntity userSessionEntity =jwtAuthenticationUtil.getRefreshTokenUserSession(refreshRequestDTO.getRefreshToken())
				.orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
		userSessionEntity = authenticationService.refresh(userSessionEntity);
		String accessToken = jwtAuthenticationUtil.generateAccessToken(userSessionEntity);
		String refreshToken = jwtAuthenticationUtil.generateRefreshToken(userSessionEntity);
		return ResponseEntity.ok(
				new LoginResponseDTO(accessToken, refreshToken, jwtAuthenticationUtil.getAccessTokenExpirationTime()));
	}

	@GetMapping("/logout")
	public ResponseEntity<LoginResponseDTO> login() {
		UserSessionEntity userSessionEntity = userSessionContext.getUserSession()
				.orElseThrow(() -> new BadCredentialsException("User session not found"));
		authenticationService.logout(userSessionEntity);
		return ResponseEntity.ok().build();
	}

}
