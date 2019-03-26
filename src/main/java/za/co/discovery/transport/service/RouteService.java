package za.co.discovery.transport.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import za.co.discovery.transport.entity.Route;
import za.co.discovery.transport.exception.RouteExistException;
import za.co.discovery.transport.repository.RouteRepository;

@Service
public class RouteService {
	
	@Autowired
	RouteRepository routeRepository;
	
	@Value(value="${insertLimit}")
	String INSERT_LIMIT;

	/*this method finds all available routes
	 * @return Return list of routes object
	 * */	
	public List<Route> findAllRoutes() {		
		return routeRepository.findAll();
	}

	/*
	 * Save inputRoutes object in batch
	 * @param inputRoutes
	 * Exception for RouteExistException() is handle by RouteExceptionController.class
	 * */
	public ResponseEntity<Object> saveRoute(List<Route> inputRoutes) throws RouteExistException{
		
			List<Route> databaseRoutes = findAllRoutes();
			
			for(Route inputRoute : inputRoutes) {
				for(Route dataRoute : databaseRoutes) {
					
					String inputOrigin = inputRoute.getOrigin().getPlanetName();
					String databaseOrigin = dataRoute.getOrigin().getPlanetName();
					String inputDestination = inputRoute.getDestination().getPlanetName();
					String databaseDesitnation = dataRoute.getDestination().getPlanetName();
					
					if(inputOrigin.equalsIgnoreCase(databaseOrigin) && inputDestination.equalsIgnoreCase(databaseDesitnation) ||
					inputOrigin.equalsIgnoreCase(databaseDesitnation) && inputDestination.equalsIgnoreCase(databaseOrigin)) {
						throw new RouteExistException();
					}
				}			
			}
			
			Iterator<Route> itr = inputRoutes.iterator();
			while(itr.hasNext()) {
				int i= 0;
				while(itr.hasNext() && i< Integer.parseInt(INSERT_LIMIT)) {
					Route route = itr.next();
					routeRepository.save(route);
					i++;
				}
			}
			
			return new ResponseEntity<Object>(inputRoutes, HttpStatus.CREATED);		
	}

	/*this method finds route based on id passed
	 * @return Return route object
	 * */
	public Route findRouteById(long id) {
		return routeRepository.findById(id).orElse(null);
		
	}

	
	/*this method delete route based on id passed*/
	public boolean deleteRouteById(long id) {
		routeRepository.deleteById(id);		
		return true;
		
	}

	/*this method update route based on id passed*/
	public boolean updateRoute(Route dataRoute) {
		routeRepository.save(dataRoute);
		
		return true;
		
	}

}
