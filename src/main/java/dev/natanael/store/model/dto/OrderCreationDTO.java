package dev.natanael.store.model.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class OrderCreationDTO {

	@NotNull
	private Long clientId;

	@NotEmpty
	private List<OrderItemCreationDTO> items;

	@NotNull
	@Positive
	private Integer installments;

}