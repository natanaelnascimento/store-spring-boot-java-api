package dev.natanael.store.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class OrderItemCreationDTO {

	@NotNull
	private Long productId;

	@NotNull
	@Positive
	private Integer quantity;

}