package dev.natanael.store.model.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class OfficeHourResponseDTO {

	private Long id;
	private Integer dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;

}
