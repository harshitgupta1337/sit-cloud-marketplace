package org.sit.cloud.marketplace.entities;

import java.util.UUID;

public class Transaction {
	
	private String vmId;
	
	private double availability;
	private double cost;
	private double bw;
	private double requestedAvailability;
	private double requestedBandwidth;
	private double maxAffordableCost;
	
	private String id;
	
	private String userId;
	
	public Transaction(String userId, String vmId, double availability, double cost, double bw, double requestedAvailability, double requestedBandwidth, double maxAffordableCost){
		this.userId = userId;
		this.vmId = vmId;
		this.availability = availability;
		this.cost = cost;
		this.bw = bw;
		this.id = UUID.randomUUID().toString();
		this.requestedAvailability = requestedAvailability;
		this.requestedBandwidth = requestedBandwidth;
		this.maxAffordableCost = maxAffordableCost;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public double getAvailability() {
		return availability;
	}

	public void setAvailability(double availability) {
		this.availability = availability;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getBw() {
		return bw;
	}

	public void setBw(double bw) {
		this.bw = bw;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getRequestedAvailability() {
		return requestedAvailability;
	}

	public void setRequestedAvailability(double requestedAvailability) {
		this.requestedAvailability = requestedAvailability;
	}

	public double getRequestedBandwidth() {
		return requestedBandwidth;
	}

	public void setRequestedBandwidth(double requestedBandwidth) {
		this.requestedBandwidth = requestedBandwidth;
	}

	public double getMaxAffordableCost() {
		return maxAffordableCost;
	}

	public void setMaxAffordableCost(double maxAffordableCost) {
		this.maxAffordableCost = maxAffordableCost;
	}

}
