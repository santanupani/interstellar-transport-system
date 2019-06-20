package za.co.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import za.co.transport.exception.RouteExistException;
import za.co.transport.exception.RouteNotExistException;

@ControllerAdvice
public class RouteExceptionController {
	/*
	 * Throw exception if route already exist
	 * */	
	@ExceptionHandler(value = RouteExistException.class)
	public ResponseEntity<Object> routeExistException(RouteExistException ex){
		return new ResponseEntity<>("Route Already Exist", HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * Throw exception if route not exist
	 * */	
	@ExceptionHandler(value = RouteNotExistException.class)
	public ResponseEntity<Object> routeNotFoundException(RouteNotExistException ex){
		return new ResponseEntity<>("Route Not Exist", HttpStatus.NOT_FOUND);
	}

}
