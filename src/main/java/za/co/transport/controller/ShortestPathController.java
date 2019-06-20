package za.co.transport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.transport.entity.RouteRequest;
import za.co.transport.service.ShortestPathService;

@RestController
@RequestMapping("/api/shortestPath")
public class ShortestPathController {

	@Autowired
	@Lazy
	private ShortestPathService shortestPathService;


	@GetMapping(value = "/{origin}/{destination}")
	public ResponseEntity<Object> findShortestPath(@PathVariable("origin") String origin, @PathVariable("destination") String destination) throws Exception {
		if(origin.equalsIgnoreCase(destination)) {
			throw new Exception("Origin and Destination is same");
		}
		String shortRoute =  shortestPathService.findShortestPath(origin, destination);
		
		RouteRequest routeRequest = new RouteRequest();
		routeRequest.setOrigin(origin);
		routeRequest.setDestination(destination);
		routeRequest.setResult(shortRoute);
		
		shortestPathService.saveRequest(routeRequest);
		return new ResponseEntity<Object>(routeRequest, HttpStatus.OK);
	}
}
