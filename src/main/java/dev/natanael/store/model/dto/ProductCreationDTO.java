package dev.natanael.store.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ProductCreationDTO {

	@NotBlank
	@Length(min = 2, max = 100)
	private String name;

	@NotBlank
	@Length(min = 5, max = 255)
	private String description;

	@NotNull
	@Positive
	private BigDecimal price;

}
