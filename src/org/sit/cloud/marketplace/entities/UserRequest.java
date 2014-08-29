package org.sit.cloud.marketplace.entities;

import java.util.Map;

public class UserRequest {
	
	private String userId;
	
	private Map<GeoLocation, Integer> geoLocationToNumOfVmsMap;
	
	public UserRequest(String userId, Map<GeoLocation, Integer> geoLocationToNumOfVmsMap){
		this.userId = userId;
		this.geoLocationToNumOfVmsMap = geoLocationToNumOfVmsMap;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<GeoLocation, Integer> getGeoLocationToNumOfVmsMap() {
		return geoLocationToNumOfVmsMap;
	}

	public void setGeoLocationToNumOfVmsMap(Map<GeoLocation, Integer> geoLocationToNumOfVmsMap) {
		this.geoLocationToNumOfVmsMap = geoLocationToNumOfVmsMap;
	}
}
