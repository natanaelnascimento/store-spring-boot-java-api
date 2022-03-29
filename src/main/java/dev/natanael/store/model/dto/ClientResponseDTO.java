package dev.natanael.store.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClientResponseDTO {

	private Long id;
	private String name;
	private String address;
	private BigDecimal creditLimit;
	private Integer installmentsLimit;

}
