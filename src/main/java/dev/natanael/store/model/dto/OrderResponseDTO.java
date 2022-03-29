package dev.natanael.store.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponseDTO {

	private Long id;
	private ClientResponseDTO client;
	private UserResponseDTO user;
	private List<OrderItemResponseDTO> items;
	private LocalDateTime dateTime;
	private BigDecimal discount;
	private Integer installments;

}