package dev.natanael.store.exception;

import lombok.Getter;

public class InstallmentsLimitOverflowException extends BusinessException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final String error = "installments_limit_overflow";

}
