package dev.natanael.store.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponseDTO {

	private Long id;
	private ProductResponseDTO product;
	private BigDecimal price;
	private Integer quantity;

}