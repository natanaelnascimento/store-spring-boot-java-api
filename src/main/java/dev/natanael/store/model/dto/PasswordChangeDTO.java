package dev.natanael.store.model.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PasswordChangeDTO {

	@NotBlank
	private String currentPassword;

	@NotBlank
	private String newPassword;

}
