package dev.natanael.store.exception;

import lombok.Getter;

public class OutsideWorkingDaysException extends BusinessException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final String error = "outside_working_days";

}
