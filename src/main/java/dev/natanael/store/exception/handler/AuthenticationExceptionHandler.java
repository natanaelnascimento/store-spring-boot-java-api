package dev.natanael.store.exception.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.natanael.store.model.dto.ErrorResponseDTO;
import dev.natanael.store.util.JwtAuthenticationUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtAuthenticationUtil jwtAuthenticationUtil;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		ErrorResponseDTO errorResponse = request.getHeader(jwtAuthenticationUtil.getAccessTokenHeaderName()) == null
				? new ErrorResponseDTO("not_authenticated") : new ErrorResponseDTO("bad_credentials");

		log.warn(exception);
		
		objectMapper.writeValue(response.getOutputStream(), errorResponse);
	}

}