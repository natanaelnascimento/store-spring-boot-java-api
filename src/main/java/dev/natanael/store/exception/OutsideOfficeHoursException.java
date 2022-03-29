package dev.natanael.store.exception;

import lombok.Getter;

public class OutsideOfficeHoursException extends BusinessException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final String error = "outside_office_hours";

}
