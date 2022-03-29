package dev.natanael.store.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginResponseDTO {

	@NonNull
	private String accessToken;

	@NonNull
	private String refreshToken;

	@NonNull
	private Long expires;

}
