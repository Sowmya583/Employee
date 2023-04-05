package com.example.Employees.Exceptions;

import java.util.NoSuchElementException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EmptyInputException.class)
    public ResponseEntity<Object> handleEmptyInputException(
    		EmptyInputException ex) {

        return new ResponseEntity<>("Input field cannot be Empty,Please check!!", HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNodataFoundException(
        NoDataFoundException ex) {

        return new ResponseEntity<>("No Records Available!!", HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(
    		EmptyResultDataAccessException ex) {

        return new ResponseEntity<>("No Records Exists To delete,please check the ID!!", HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(
    		NoSuchElementException ex) {

        return new ResponseEntity<>("No Records Exists with the given ID!!", HttpStatus.NOT_FOUND);
    }
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
	
		return new ResponseEntity<Object>("Please change your http method type!!", HttpStatus.METHOD_NOT_ALLOWED);
	}

}
