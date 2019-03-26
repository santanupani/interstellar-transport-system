package za.co.discovery.transport.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.discovery.transport.entity.Planet;
import za.co.discovery.transport.entity.Route;
import za.co.discovery.transport.service.PlanetService;
import za.co.discovery.transport.service.RouteService;
import za.co.discovery.transport.service.TransportUtility;

@RestController
@RequestMapping("/api/routes")
public class RouterController {
	
	@Autowired
	RouteService routeService;
	
	@Autowired
	PlanetService planetService;
	
	List<Route> routeList = null;
	
	@PostMapping
	public ResponseEntity<Object> createRoute(@RequestBody List<Route> routes) throws Exception{
		
		routeList = new ArrayList<Route>();
		
			for(Route route: routes) {
				Planet origin = planetService.getPlanetById(route.getOrigin().getPlanetId(), TransportUtility.getRequestType(0));
				Planet destination = planetService.getPlanetById(route.getDestination().getPlanetId(), TransportUtility.getRequestType(0));
				
				route.setOrigin(origin);
				route.setDestination(destination);
				
				routeList.add(route);
				
			}
			
			return routeService.saveRoute(routeList);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Object> getRoute(@PathVariable("id") long id){
		
		Route route = routeService.findRouteById(id);		
		return new ResponseEntity<>(route, HttpStatus.OK);
		
	}
	
	@GetMapping()
	public ResponseEntity<Object> getAllRoute(){
		
		List<Route> routes = routeService.findAllRoutes();		
		return new ResponseEntity<>(routes, HttpStatus.OK);
		
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Object> deleteRouteById(@PathVariable("id") long id){
		ResponseEntity<Object> response = null;
		
		boolean isDeleted = routeService.deleteRouteById(id);
		if(isDeleted) {
			response = new ResponseEntity<>("Route Object deleted by id :"+id, HttpStatus.OK);
		}
		
		return response;
		
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Object> updateRouteById(@PathVariable("id") long id, @RequestBody Route route){
		
		ResponseEntity<Object> response = null;
		Route dataRoute = routeService.findRouteById(id);
		
		if(dataRoute != null) {
			dataRoute.setDistance(route.getDistance());
			dataRoute.setOrigin(route.getOrigin());
			dataRoute.setDestination(route.getDestination());
			
			boolean isUpdated = routeService.updateRoute(dataRoute);
			
			if(isUpdated) {
				response = new ResponseEntity<>(dataRoute, HttpStatus.OK);
			}
		}

		return response;
		
	}

}
