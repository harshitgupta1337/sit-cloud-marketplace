package org.sit.cloud.marketplace.entities;

import java.util.UUID;

public class Vm {

	private String id;
	private String providerId;
	private GeoLocation geoLocation;
	
	private double experience;
	
	public Vm(){
		id = UUID.randomUUID().toString();
		providerId = null;
		setGeoLocation(null);
		setExperience(1.0);
	}
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}
	
}
