package za.co.discovery.transport.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.discovery.transport.entity.Planet;
import za.co.discovery.transport.service.PlanetService;
import za.co.discovery.transport.service.TransportUtility;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

	@Autowired
	PlanetService planetService;
	
	@PostMapping()
	public ResponseEntity<Object> createPlanet(@RequestBody List<Planet> planets){
		return planetService.savePlanet(planets);
	}
	
	@GetMapping(value= "/{planetId}")
	public ResponseEntity<Object> getPlanet(@PathVariable("planetId") String planetId){
		Planet planet = planetService.getPlanetById(planetId, TransportUtility.getRequestType(0));
		return new ResponseEntity<>(planet, HttpStatus.OK);
		
	}
	
	@GetMapping()
	public ResponseEntity<Object> getAllPlanet(){
		List<Planet> planets = planetService.findAllPlanets();
		return new ResponseEntity<>(planets, HttpStatus.OK);
		
	}
}
