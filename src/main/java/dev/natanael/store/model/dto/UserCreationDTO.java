package dev.natanael.store.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserCreationDTO {

	@NotBlank
	@Length(min = 2, max = 100)
	private String name;

	@NotBlank
	@Pattern(regexp = "[a-zA-z\\d_]+", message="Only letters, numbers and underscore allowed")
	private String username;

	@NotBlank
	@Length(min = 8, max = 30)
	private String password;

}