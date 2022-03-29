package dev.natanael.store.model.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RefreshRequestDTO {

	@NotBlank
	private String refreshToken;

}
