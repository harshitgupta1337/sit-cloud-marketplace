package org.sit.cloud.marketplace.entities;

import java.util.Map;

public class ProviderParams {
	
	private String providerId;
	private double availability;
	private double cost;
	private double bw;
	private Map<GeoLocation, Integer> availableVmsMap;
	private int cores;
	private int ram;
	private int storage;
	
	public ProviderParams(String providerId, double availability, double cost, double bw, int cores, int ram, int storage){
		this.providerId = providerId;
		this.availability = availability;
		this.cost = cost;
		this.bw = bw;
		this.cores = cores;
		this.ram = ram;
		this.storage = storage;
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
	public Map<GeoLocation, Integer> getAvailableVmsMap() {
		return availableVmsMap;
	}
	public void setAvailableVmsMap(Map<GeoLocation, Integer> availableVmsMap) {
		this.availableVmsMap = availableVmsMap;
	}
	/**
	 * @return the cores
	 */
	public int getCores() {
		return cores;
	}
	/**
	 * @param cores the cores to set
	 */
	public void setCores(int cores) {
		this.cores = cores;
	}
	/**
	 * @return the ram (in MB)
	 */
	public int getRam() {
		return ram;
	}
	/**
	 * @param ram the ram to set (in MB)
	 */
	public void setRam(int ram) {
		this.ram = ram;
	}
	/**
	 * @return the storage (in GB)
	 */
	public int getStorage() {
		return storage;
	}
	/**
	 * @param storage the storage to set (in GB)
	 */
	public void setStorage(int storage) {
		this.storage = storage;
	}
	/**
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}
	/**
	 * @param providerId the providerId to set
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	
}
