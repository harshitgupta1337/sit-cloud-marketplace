package org.sit.cloud.marketplace.entities;

public class ProviderParams {
	
	private String providerId;
	private double availability;
	private double cost;
	private double bw;
	private int numOfVmsAvailable;
	private int cores;
	private int ram;
	private int storage;
	private float fuzzyUtility;
	private double trustInAvailability;
	private double trustInBandwidth;
	
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

	public int getNumOfVmsAvailable() {
		return numOfVmsAvailable;
	}

	public void setNumOfVmsAvailable(int numOfVmsAvailable) {
		this.numOfVmsAvailable = numOfVmsAvailable;
	}

	public float getFuzzyUtility() {
		return fuzzyUtility;
	}

	public void setFuzzyUtility(float fuzzyUtility) {
		this.fuzzyUtility = fuzzyUtility;
	}

	public double getTrustInBandwidth() {
		return trustInBandwidth;
	}

	public void setTrustInBandwidth(double trustInBandwidth) {
		this.trustInBandwidth = trustInBandwidth;
	}

	public double getTrustInAvailability() {
		return trustInAvailability;
	}

	public void setTrustInAvailability(double trustInAvailability) {
		this.trustInAvailability = trustInAvailability;
	}
	
	
}
