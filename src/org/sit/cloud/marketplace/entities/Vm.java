package org.sit.cloud.marketplace.entities;

import java.util.UUID;

public class Vm {

	private static int VM_ID = 0;
	
	private String id;
	private String providerId;
	
	private double experience;
	
	private boolean shouldBeViolated;
	
	public Vm(boolean shouldBeViolated){
		id = Integer.toString(++VM_ID);
		providerId = null;
		setExperience(1.0);
		this.shouldBeViolated = shouldBeViolated;
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

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public boolean isShouldBeViolated() {
		return shouldBeViolated;
	}

	public void setShouldBeViolated(boolean shouldBeViolated) {
		this.shouldBeViolated = shouldBeViolated;
	}
	
}
