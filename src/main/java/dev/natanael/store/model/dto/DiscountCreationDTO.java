package dev.natanael.store.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class DiscountCreationDTO {

	@NotBlank
	@Length(min = 2, max = 100)
	private String description;

	@NotNull
	@Positive
	@Max(1)
	private BigDecimal percentage;

	@NotNull
	@Positive
	private Integer installmentsLimit;

}
