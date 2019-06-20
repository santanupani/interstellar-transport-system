package za.co.transport.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import za.co.transport.entity.Planet;
import za.co.transport.entity.Route;
import za.co.transport.entity.RouteRequest;
import za.co.transport.repository.PlanetRepository;
import za.co.transport.repository.RouteRepository;
import za.co.transport.repository.RouteRequestRepository;

@Service
@Lazy
public class ShortestPathServiceImp implements ShortestPathService{
	
	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private RouteRequestRepository routeRequestRepository;
	
    private List<Route> routes;
    private Set<Planet> settledPlanets;
    private Set<Planet> unSettledPlanets;
    private Map<Planet, Planet> predecessors;
    private Map<Planet, Integer> distance;
    
    /*Populates Route objects*/
    
    @PostConstruct
    private void init() {
    	routes = routeRepository.findAll();
    }
    
    /**
	 * This method finds shortest way between plantes.
	 * @param origin
	 * @param destination
	 * @return Returns string subplanetRoute path in string
	 */
	@SuppressWarnings("null")
	public String findShortestPath(String origin, String destination) {
		Planet source = planetRepository.findById(origin).get();
		
		settledPlanets = new HashSet<Planet>();
		unSettledPlanets = new HashSet<Planet>();
		
        distance = new HashMap<Planet, Integer>();
        predecessors = new HashMap<Planet, Planet>();
        distance.put(source, 0);
        unSettledPlanets.add(source);
        
        while (unSettledPlanets.size() > 0) {
        	Planet planet = getMinimum(unSettledPlanets);
        	settledPlanets.add(planet);
        	unSettledPlanets.remove(planet);
            findMinimalDistances(planet);
        }
        
        Planet target = planetRepository.findById(destination).get();
        
        LinkedList<Planet> path = getPath(target);
        String planetRoute = "";
        
        for (Planet planet : path) {
            planetRoute+=planet.getPlanetName() +" >";
        }
        
        String subplanetRoute = planetRoute.substring(0, (planetRoute.length()-1));
		return subplanetRoute.trim();
		
	}
	
	private void findMinimalDistances(Planet planet) {
		List<Planet> adjacentPlanets = getNeighbors(planet);
        for (Planet target : adjacentPlanets) {
            if (getShortestDistance(target) > getShortestDistance(planet)
                    + getDistance(planet, target)) {
                distance.put(target, getShortestDistance(planet)
                        + getDistance(planet, target));
                predecessors.put(target, planet);
                unSettledPlanets.add(target);
            }
        }
		
	}
	
	private int getDistance(Planet node, Planet target) {
		for (Route route : routes) {
            if (route.getOrigin().equals(node)
                    && route.getDestination().equals(target)) {
                return (int) route.getDistance();
            }
        }
        throw new RuntimeException("Should not happen");
	}
	
	private List<Planet> getNeighbors(Planet planet) {
		 List<Planet> neighbors = new ArrayList<Planet>();
	        for (Route route : routes) {
	            if (route.getOrigin().equals(planet)
	                    && !isSettled(route.getDestination())) {
	                neighbors.add(route.getDestination());
	            }
	        }
	        return neighbors;
	}
	
	private boolean isSettled(Planet destination) {
		return settledPlanets.contains(destination);
	}
	
	private Planet getMinimum(Set<Planet> unSettledPlanetSet) {
		Planet minimum = null;
        for (Planet planet : unSettledPlanetSet) {
            if (minimum == null) {
                minimum = planet;
            } else {
                if (getShortestDistance(planet) < getShortestDistance(minimum)) {
                    minimum = planet;
                }
            }
        }
        return minimum;
	}
	
	private int getShortestDistance(Planet destination) {
		Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
	}
	
	private LinkedList<Planet> getPath(Planet target) {
        LinkedList<Planet> path = new LinkedList<Planet>();
        Planet step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }
	
	/*This method saves shortest path string value in database*/
	@Override
	public void saveRequest(RouteRequest routeRequest) {
		routeRequestRepository.save(routeRequest);
		
	}
	
	
	/*This method set collection objects to null which will be ready for garbage collection*/ 
	@PreDestroy
	private void destroy() {
		routes = null;
	    settledPlanets = null;
	    unSettledPlanets = null;
	    predecessors = null;
	    distance = null;
		
	}

}
