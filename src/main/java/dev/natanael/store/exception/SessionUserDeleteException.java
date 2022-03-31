package dev.natanael.store.exception;

import lombok.Getter;


public class SessionUserDeleteException extends BusinessException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final String error = "session_user_delete";
	
}
