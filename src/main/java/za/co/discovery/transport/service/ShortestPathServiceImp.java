package za.co.discovery.transport.service;

import java.util.List;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import za.co.discovery.transport.entity.Planet;
import za.co.discovery.transport.entity.Route;
import za.co.discovery.transport.entity.RouteRequest;
import za.co.discovery.transport.exception.RouteNotExistException;
import za.co.discovery.transport.repository.RouteRequestRepository;

/*
* ShortestPathServiceImp returns the shortest path between two nodes in a graph.
* Here we are using DijkstraAlgorithm that calculates the shortest path. It only initialized once when the service is invoked.
* We initialize this service class on request. We use @Lazy.
* We will be loading vertext and edge before any business method invokes. We use @PostConstruct
* The object of DijkstraAlgorithm set to null after service for garbage collection. We use @PreDestroy
*/

@Service
@Lazy
public class ShortestPathServiceImp implements ShortestPathService {
	
	@Autowired
	RouteService routeService;
	
	@Autowired
	PlanetService planetService;
	
	@Autowired
	RouteRequestRepository routeRequestRepository;
	
	/**
	 * SimpleDirectedWeightedGraph object gets initialized on class loading.
	 */
	private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> simpleDirectedWeightedGraph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	
	@PostConstruct
	private void init() {
		addVertextGraph();
		addEdgeGraph();
		
	}
	
	/*This method ads all Vertex(Planet) into graph (SimpleDirectedWeightedGraph) from database*/
	private void addVertextGraph() {
		List<Planet> planets = planetService.findAllPlanets();
		for(Planet planet : planets) {
			this.simpleDirectedWeightedGraph.addVertex(planet.getPlanetId());
		}
		
	}
	
	/**
	 * This method fetch all edges(Route) from database and initialize SimpleDirectedWeightedGraph
	 */
	private void addEdgeGraph(){
		DefaultWeightedEdge weightedEdge = null;
		List<Route> routes = routeService.findAllRoutes();
		
		for (Route route : routes) {
			String originPlanet  = route.getOrigin().getPlanetId();
			String destinationPlanet = route.getDestination().getPlanetId();
			
			if(!originPlanet.equals(destinationPlanet)){
				weightedEdge = this.simpleDirectedWeightedGraph.addEdge(originPlanet, destinationPlanet);
			}
			
			addDistanceEdge(weightedEdge, route.getDistance());
		}
	}	

	/**
	 * This method ad distance to edge(Route).
	 * @param weightedEdge This is the Route object which adding distance
	 * @param distance  This is the distance that to added to edge.
	 */
	private void addDistanceEdge(DefaultWeightedEdge weightedEdge, double distance) {
		this.simpleDirectedWeightedGraph.setEdgeWeight(weightedEdge, distance);
		
	}

	/**
	 * This method returns shortest path between node.
	 * @param origin  Origin Planet
	 * @param destination   Desstination Planet
	 * @return shortest path in string
	 */
	@Override
	public String findShortestPath(String origin, String destination) {
		
		return  DijkstraShortestPath.findPathBetween(this.simpleDirectedWeightedGraph, origin, destination).toString();
		
	}
	
	
	
	/*This method saves shortest path string value in database*/
	@Override
	public void saveRequest(RouteRequest routeRequest) {
		routeRequestRepository.save(routeRequest);
		
	}

	/**
	 * Set SimpleDirectedWeightedGraph to null for garbage collection.
	 */
	@PreDestroy
	public void destroy(){
		this.simpleDirectedWeightedGraph = null;
	}

}
