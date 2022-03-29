package dev.natanael.store.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DiscountResponseDTO {

	private Long id;
	private String description;
	private BigDecimal percentage;
	private Integer installmentsLimit;

}
