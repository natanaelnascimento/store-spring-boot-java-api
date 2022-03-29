package dev.natanael.store.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ClientCreationDTO {

	@NotBlank
	@Length(min = 2, max = 100)
	private String name;

	@NotBlank
	@Length(min = 2, max = 100)
	private String address;

	@NotNull
	@Positive
	private BigDecimal creditLimit;

	@NotNull
	@Positive
	private Integer installmentsLimit;

}
