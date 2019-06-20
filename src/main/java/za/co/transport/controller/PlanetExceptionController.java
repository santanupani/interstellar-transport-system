package za.co.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import za.co.transport.exception.PlanetExistException;
import za.co.transport.exception.PlanetNotFoundException;

@ControllerAdvice
public class PlanetExceptionController {
	
	/*
	 * Throw exception if planet already exist
	 * */	
	@ExceptionHandler(value = PlanetExistException.class)
	public ResponseEntity<Object> planetExistException(PlanetExistException ex){
		return new ResponseEntity<>("Planet Already Exist", HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * Throw exception if planet not found
	 * */	
	@ExceptionHandler(value = PlanetNotFoundException.class)
	public ResponseEntity<Object> planetNotFoundException(PlanetNotFoundException ex){
		return new ResponseEntity<>("Planet Not Found", HttpStatus.NOT_FOUND);
	}

}
