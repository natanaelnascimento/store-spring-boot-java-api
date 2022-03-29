package dev.natanael.store.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.natanael.store.util.JwtAuthenticationUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApi30Configuration {

	@Autowired
	private JwtAuthenticationUtil jwtAuthenticationUtil;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes(jwtAuthenticationUtil.getAccessTokenHeaderName(),
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
								.in(SecurityScheme.In.HEADER).name(jwtAuthenticationUtil.getAccessTokenHeaderName())))
				.info(new Info().title("Store API").version("v1")).addSecurityItem(new SecurityRequirement()
						.addList(jwtAuthenticationUtil.getAccessTokenHeaderName(), Arrays.asList("read", "write")));
	}

}