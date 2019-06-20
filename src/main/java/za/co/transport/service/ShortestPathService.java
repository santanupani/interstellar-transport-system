package za.co.transport.service;

import za.co.transport.entity.RouteRequest;

public interface ShortestPathService {
	public String findShortestPath(String origin, String destination);

	public void saveRequest(RouteRequest routeRequest);
}
