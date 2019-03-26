package za.co.discovery.transport.service;

import za.co.discovery.transport.entity.RouteRequest;

public interface ShortestPathService {
	public String findShortestPath(String origin, String destination);

	public void saveRequest(RouteRequest routeRequest);
}
