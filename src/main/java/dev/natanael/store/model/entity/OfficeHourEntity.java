package dev.natanael.store.model.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_office_hour")
public class OfficeHourEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Min(1)
	@Max(7)
	@Column(nullable = false)
	private Integer dayOfWeek;

	@NotNull
	@Column(nullable = false)
	private LocalTime startTime;

	@NotNull
	@Column(nullable = false)
	private LocalTime endTime;

}