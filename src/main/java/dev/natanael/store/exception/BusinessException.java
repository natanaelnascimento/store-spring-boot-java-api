package dev.natanael.store.exception;

public abstract class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public abstract String getError();

}
