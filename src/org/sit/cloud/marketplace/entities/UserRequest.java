package org.sit.cloud.marketplace.entities;

public class UserRequest {
	
	private static int ID = 0;
	
	private String id;
	
	private String userId;
	
	private int numOfVms;
	
	private double requiredAvailability;
	
	private double requiredBandwidth;
	
	private double maxAffordableCost;
	
	private int cores;
	
	private int ram;
	
	private int storage;
	
	/**
	 * @return the requiredAvailability
	 */
	public double getRequiredAvailability() {
		return requiredAvailability;
	}

	/**
	 * @param requiredAvailability the requiredAvailability to set
	 */
	public void setRequiredAvailability(double requiredAvailability) {
		this.requiredAvailability = requiredAvailability;
	}

	/**
	 * @return the requiredBandwidth
	 */
	public double getRequiredBandwidth() {
		return requiredBandwidth;
	}

	/**
	 * @param requiredBandwidth the requiredBandwidth to set
	 */
	public void setRequiredBandwidth(double requiredBandwidth) {
		this.requiredBandwidth = requiredBandwidth;
	}

	/**
	 * @return the maxAffordableCost
	 */
	public double getMaxAffordableCost() {
		return maxAffordableCost;
	}

	/**
	 * @param maxAffordableCost the maxAffordableCost to set
	 */
	public void setMaxAffordableCost(double maxAffordableCost) {
		this.maxAffordableCost = maxAffordableCost;
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
	 * @return the ram
	 */
	public int getRam() {
		return ram;
	}

	/**
	 * @param ram the ram to set
	 */
	public void setRam(int ram) {
		this.ram = ram;
	}

	/**
	 * @return the storage
	 */
	public int getStorage() {
		return storage;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(int storage) {
		this.storage = storage;
	}

	/**
	 * A marker assigned to each UserRequest that tells whether the VMs created by processing this request will face SLA violation or not.
	 * This is strictly for the purpose of simulation.
	 */
	private boolean shouldBeViolated;
	
	public UserRequest(String userId, int numOfVms, double requiredAvailability, double requiredBandwidth, int cores, int ram, int storage, double maxAffordableCost, boolean shouldBeViolated){
		this.setId(Integer.toString(++ID));
		this.userId = userId;
		this.shouldBeViolated = shouldBeViolated;
		this.cores = cores;
		this.ram = ram;
		this.storage = storage;
		this.maxAffordableCost = maxAffordableCost;
		this.requiredAvailability = requiredAvailability;
		this.requiredBandwidth = requiredBandwidth;
		this.setNumOfVms(numOfVms);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isShouldBeViolated() {
		return shouldBeViolated;
	}

	public void setShouldBeViolated(boolean shouldBeViolated) {
		this.shouldBeViolated = shouldBeViolated;
	}

	public int getNumOfVms() {
		return numOfVms;
	}

	public void setNumOfVms(int numOfVms) {
		this.numOfVms = numOfVms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
