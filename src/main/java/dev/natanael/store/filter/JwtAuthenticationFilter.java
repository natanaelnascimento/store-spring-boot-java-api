package dev.natanael.store.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.util.JwtAuthenticationUtil;
import dev.natanael.store.util.UserSessionContext;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtAuthenticationUtil jwtAuthenticationUtil;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private UserSessionContext userSessionContext;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader(jwtAuthenticationUtil.getAccessTokenHeaderName());
		if (token != null && token.contains(" "))
			token = token.substring(token.indexOf(" "));

		jwtAuthenticationUtil.getAccessTokenUserSession(token).ifPresent(userSessionEntity -> {
			UserEntity userEntity = userSessionEntity.getUser();

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userEntity, null, Collections.emptyList());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

			userSessionContext.setUserSession(userSessionEntity);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		});

		filterChain.doFilter(request, response);
	}

}
