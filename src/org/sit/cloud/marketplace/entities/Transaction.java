package org.sit.cloud.marketplace.entities;

import java.util.List;
import java.util.UUID;

public class Transaction {
	
	private List<String> vmIds;
	
	private double availability;
	private double cost;
	private double bw;
	
	private String id;
	
	private String userId;
	
	public Transaction(String userId, List<String> vmIds, double availability, double cost, double bw){
		this.userId = userId;
		this.vmIds = vmIds;
		this.availability = availability;
		this.cost = cost;
		this.bw = bw;
		this.id = UUID.randomUUID().toString();
	}

	public List<String> getVmIds() {
		return vmIds;
	}

	public void setVmIds(List<String> vmIds) {
		this.vmIds = vmIds;
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

}
