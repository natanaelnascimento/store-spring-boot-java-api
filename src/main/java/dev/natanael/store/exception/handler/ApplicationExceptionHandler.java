package dev.natanael.store.exception.handler;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import dev.natanael.store.exception.BusinessException;
import dev.natanael.store.model.dto.ErrorResponseDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException exception) {
		log.info(exception);
		return new ResponseEntity<>(new ErrorResponseDTO(exception.getError()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<Object> badCredentialsException(BadCredentialsException exception) {
		log.warn(exception);
		return new ResponseEntity<>(new ErrorResponseDTO("bad_credentials"), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = EntityNotFoundException.class)
	public ResponseEntity<Object> entityNotFoundException(EntityNotFoundException exception) {
		log.info(exception);
		return new ResponseEntity<>(new ErrorResponseDTO("not_found"), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.info(exception);
		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		return new ResponseEntity<>(new ErrorResponseDTO(errors, "data_validation"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	protected ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
		log.info(exception);
		return new ResponseEntity<>(new ErrorResponseDTO(null, "structure_validation"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> httpMessageNotReadableException(MethodArgumentTypeMismatchException exception) {
		log.info(exception);
		return new ResponseEntity<>(new ErrorResponseDTO(null, "structure_validation"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PropertyReferenceException.class)
	public ResponseEntity<Object> propertyReferenceException(PropertyReferenceException exception) {
		log.info(exception);
		Map<String, String> errors = new HashMap<>();
		errors.put(exception.getPropertyName(), exception.getMessage());
		return new ResponseEntity<>(new ErrorResponseDTO(errors, "structure_validation"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataIntegrityViolationException.class)
	public ResponseEntity<Object> propertyReferenceException(DataIntegrityViolationException exception) {
		log.info(exception);
		return new ResponseEntity<>(new ErrorResponseDTO("integrity_violation"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> throwable(Throwable throwable) {
		log.error(throwable);
		return new ResponseEntity<>(new ErrorResponseDTO("server_error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}