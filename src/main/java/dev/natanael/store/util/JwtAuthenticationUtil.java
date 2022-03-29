package dev.natanael.store.util;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.model.entity.UserSessionEntity;
import dev.natanael.store.service.UserSessionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtParser;
import lombok.Getter;

@Component
public class JwtAuthenticationUtil {

	@Autowired
	private UserSessionService userSessionService;

	@Getter
	@Value("${store.jwt.refresh-token.expiration}")
	private long refreshTokenExpirationTime;

	@Getter
	@Value("${store.jwt.access-token.expiration}")
	private long accessTokenExpirationTime;

	@Getter
	@Value("${store.jwt.access-token.header-name}")
	private String accessTokenHeaderName;

	@Getter
	@Value("${store.jwt.claim.session-id}")
	private String sessionIdClaimName;

	@Getter
	@Value("${store.jwt.claim.user-id}")
	private String userIdClaimName;

	@Getter
	@Value("${store.jwt.claim.user-name}")
	private String userNameClaimName;

	public String generateAccessToken(UserSessionEntity userSessionEntity) {
		UserEntity userEntity = userSessionEntity.getUser();

		Map<String, Object> claims = new HashMap<>();
		claims.put(sessionIdClaimName, userSessionEntity.getId().toString());
		claims.put(userIdClaimName, userEntity.getId().toString());
		claims.put(userNameClaimName, userEntity.getName());
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
				.signWith(SignatureAlgorithm.HS512, userSessionEntity.getAccessTokenSecret())
				.compact();
		return token;
	}

	public String generateRefreshToken(UserSessionEntity userSessionEntity) {
		UserEntity userEntity = userSessionEntity.getUser();

		Map<String, Object> claims = new HashMap<>();
		claims.put(sessionIdClaimName, userSessionEntity.getId().toString());
		claims.put(userIdClaimName, userEntity.getId().toString());
		claims.put(userNameClaimName, userEntity.getName());
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
				.signWith(SignatureAlgorithm.HS512, userSessionEntity.getRefreshTokenSecret())
				.compact();
		return token;
	}

	public Optional<UserSessionEntity> getAccessTokenUserSession(String token) {
		try {
			String unsignedToken = token.substring(0, token.lastIndexOf('.') + 1);
	
			Claims claims = (Claims) new DefaultJwtParser().parse(unsignedToken).getBody();
			String sessionId = (String) claims.get(sessionIdClaimName);
			UserSessionEntity userSessionEntity = userSessionService.findById(UUID.fromString(sessionId)).get();
			UserEntity userEntity = userSessionEntity.getUser();

			Jwts.parser()
				.require(userIdClaimName, userEntity.getId().toString())
				.setSigningKey(userSessionEntity.getAccessTokenSecret())
				.parseClaimsJws(token).getBody();

			return Optional.of(userSessionEntity);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public Optional<UserSessionEntity> getRefreshTokenUserSession(String token) {
		try {
			String unsignedToken = token.substring(0, token.lastIndexOf('.') + 1);
			
			Claims claims = (Claims) new DefaultJwtParser().parse(unsignedToken).getBody();
			String sessionId = (String) claims.get(sessionIdClaimName);
			UserSessionEntity userSessionEntity = userSessionService.findById(UUID.fromString(sessionId)).get();
			UserEntity userEntity = userSessionEntity.getUser();
			
			Jwts.parser()
				.require(userIdClaimName, userEntity.getId().toString())
				.setSigningKey(userSessionEntity.getRefreshTokenSecret())
				.parseClaimsJws(token).getBody();

			return Optional.of(userSessionEntity);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
