package dev.natanael.store.exception;

import lombok.Getter;


public class CreditLimitOverflowException extends BusinessException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final String error = "credit_limit_overflow";
	
}
