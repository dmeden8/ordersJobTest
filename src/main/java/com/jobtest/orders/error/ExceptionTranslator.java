package com.jobtest.orders.error;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jobtest.orders.dto.response.EntityResponse;
import com.jobtest.orders.dto.response.FieldMessage;
import com.jobtest.orders.dto.response.ResponseCode;

@ControllerAdvice
public class ExceptionTranslator {
	
	@ExceptionHandler(OrdersException.class)
	public ResponseEntity<EntityResponse> ordersException(OrdersException ex) {
		EntityResponse entityResponseDto = createErrorResponse(ex);
		return new ResponseEntity<>(entityResponseDto, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<EntityResponse> validationException(ValidationException ex) {		
		EntityResponse entityResponseDto = createErrorResponse(ex);
		return new ResponseEntity<>(entityResponseDto, HttpStatus.BAD_REQUEST);
	}
			
	@ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
	public ResponseEntity<EntityResponse> hibernateConstraintViolationException(org.hibernate.exception.ConstraintViolationException ex) {
		
		EntityResponse entityResponseDto = new EntityResponse();
		entityResponseDto.setResponseCode(ResponseCode.ERROR);
		String message = ex.getCause().getMessage();
		entityResponseDto.setResponseMessage(message.substring(message.indexOf("Detail:") + 8));
		
		return new ResponseEntity<>(entityResponseDto, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<EntityResponse> dataIntegrityViolationException(DataIntegrityViolationException ex) {
		
		EntityResponse entityResponseDto = new EntityResponse();
		entityResponseDto.setResponseCode(ResponseCode.ERROR);
		String message = ex.getCause().getCause().getMessage();
		entityResponseDto.setResponseMessage(message);
		
		return new ResponseEntity<>(entityResponseDto, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<EntityResponse> constraintViolationException(ConstraintViolationException ex) {    	

		List<FieldMessage> fieldErrors = ex.getConstraintViolations()
				.stream()
				.map(cv -> {
							FieldMessage fieldMessage = new FieldMessage();
							fieldMessage.setField(cv.getPropertyPath().toString());
							fieldMessage.setMessage(cv.getMessage());

							return fieldMessage;
							})
				.collect(Collectors.toList());
		
		Collections.sort(fieldErrors);

		EntityResponse entityResponseDto = new EntityResponse();
		entityResponseDto.setResponseCode(ResponseCode.ERROR);
		entityResponseDto.setResponseMessage("Validation failed");
		entityResponseDto.setResponseFieldMessages(fieldErrors);
		return new ResponseEntity<>(entityResponseDto, HttpStatus.BAD_REQUEST);

	}
	    
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<EntityResponse> handleDataValidationException(MethodArgumentNotValidException ex) {

		List<FieldMessage> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(a -> {
			FieldMessage fieldMessage = new FieldMessage();
			fieldMessage.setField(a.getField());
			fieldMessage.setMessage(a.getDefaultMessage());				
			return fieldMessage;
		}).collect(Collectors.toList());
		
		Collections.sort(fieldErrors);

		EntityResponse entityResponse = createErrorResponse(ex);
		entityResponse.setResponseMessage("Validation failed");
		entityResponse.setResponseFieldMessages(fieldErrors);
		return new ResponseEntity<>(entityResponse, HttpStatus.BAD_REQUEST);

	}
	
	private EntityResponse createErrorResponse(Exception ex) {
		EntityResponse entityResponseDto = new EntityResponse();
		entityResponseDto.setResponseCode(ResponseCode.ERROR);
		String message = ex.getMessage();
		entityResponseDto.setResponseMessage(message);
		return entityResponseDto;
	}

}
