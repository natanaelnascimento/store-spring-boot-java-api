package dev.natanael.store.model.dto;

import java.time.LocalTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OfficeHourCreationDTO {

	@NotNull
	@Min(1)
	@Max(7)
	private Integer dayOfWeek;

	@NotNull
	private LocalTime startTime;

	@NotNull
	private LocalTime endTime;

}
