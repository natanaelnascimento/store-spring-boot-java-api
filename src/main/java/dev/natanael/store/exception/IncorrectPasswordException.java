package dev.natanael.store.exception;

import lombok.Getter;


public class IncorrectPasswordException extends BusinessException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final String error = "incorrect_password";
	
}
