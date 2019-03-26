package za.co.discovery.transport.service;

import java.util.HashMap;
import java.util.Map;

public class TransportUtility {
	
	/*
	 * This method returns request type used while iserting Route object
	 * @param index
	 * */
	public static String getRequestType(int index) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, "WEB_REQUEST");
		map.put(1, "BOOTSTRAPPING");
		String requestType = map.get(index);
		return requestType;
	}
	
	
	

}
