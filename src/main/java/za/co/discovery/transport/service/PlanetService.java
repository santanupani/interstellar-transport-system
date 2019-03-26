package za.co.discovery.transport.service;

import java.util.Iterator;
import java.util.List;

import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import za.co.discovery.transport.entity.Planet;
import za.co.discovery.transport.exception.PlanetExistException;
import za.co.discovery.transport.exception.PlanetNotFoundException;
import za.co.discovery.transport.repository.PlanetRepository;

@Service
public class PlanetService {
	@Autowired
	PlanetRepository planetRepository;
	
	@Value(value = "${insertLimit}")
	String INSERT_LIMIT;

	/*
	 * Save inputPlanetList object in batch
	 * @param inputPlanetList
	 * Exception for PlanetExistException() is handle by PlanetExceptionController.class
	 * */
	public ResponseEntity<Object> savePlanet(List<Planet> inputPlanetList) {
		ResponseEntity<Object> response = null;
		boolean planetExit = false;
		try {
			List<Planet> databasePlanetList = planetRepository.findAll();
			
			for(Planet inputPlanet : inputPlanetList) {
				for(Planet dataPlanet : databasePlanetList) {
					if(inputPlanet.getPlanetId().equalsIgnoreCase(dataPlanet.getPlanetId()) || inputPlanet.getPlanetName().equalsIgnoreCase(dataPlanet.getPlanetName())) {
						planetExit = true;
						throw new PlanetExistException();
					}
				}			
			}
			
			Iterator<Planet> itr = inputPlanetList.iterator();
			while(itr.hasNext()) {
				int i= 0;
				while(itr.hasNext() && i< Integer.parseInt(INSERT_LIMIT)) {
					Planet planet = itr.next();
					planetRepository.save(planet);
					i++;
				}
			}
			
			response = new ResponseEntity<Object>(inputPlanetList, HttpStatus.CREATED);
		}catch (Exception e) {
			e.printStackTrace();
			if(!planetExit) {
				throw new RuntimeException("There is an error inserting Planet objects");
			}			
		}
		
		if(planetExit) {
			response = new ResponseEntity<>("Planet Already Exist", HttpStatus.BAD_REQUEST);
		}
		return response;
		
	}

	/*
	 * return planet object
	 * @param planetId
	 * Exception for PlanetNotFoundException() is handle by PlanetExceptionController.class
	 * */			
	public Planet getPlanetById(String planetId, String requestType) {
		Planet planet = null;
		
		if(requestType.equalsIgnoreCase("WEB_REQUEST")) {
			planet =  planetRepository.findById(planetId).orElseThrow(PlanetNotFoundException::new);			
		}else if(requestType.equalsIgnoreCase("BOOTSTRAPPING")) {
			planet =  planetRepository.findById(planetId).orElse(null);
		}
		
		return planet;
	}

	/*This method returns all Planets from databsae*/
	public List<Planet> findAllPlanets() {
		return planetRepository.findAll();
	}

	
	/*This method delete Planet from database*/
	public void deletePlanetById(String planetId) {
		planetRepository.deleteById(planetId);
		
	}

	
}
