package org.sit.cloud.marketplace.entities;

import java.util.Map;

public class UserRequest {
	
	private String userId;
	
	private Map<GeoLocation, Integer> geoLocationToNumOfVmsMap;
	
	private boolean shouldBeViolated;
	
	public UserRequest(String userId, Map<GeoLocation, Integer> geoLocationToNumOfVmsMap, boolean shouldBeViolated){
		this.userId = userId;
		this.geoLocationToNumOfVmsMap = geoLocationToNumOfVmsMap;
		this.shouldBeViolated = shouldBeViolated;
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

	public boolean isShouldBeViolated() {
		return shouldBeViolated;
	}

	public void setShouldBeViolated(boolean shouldBeViolated) {
		this.shouldBeViolated = shouldBeViolated;
	}
}
