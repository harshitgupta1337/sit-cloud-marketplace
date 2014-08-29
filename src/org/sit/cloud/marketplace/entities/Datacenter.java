package org.sit.cloud.marketplace.entities;

public class Datacenter {

	private String id;

	private int numOfAvailableVms;
	
	// create a list of vms in this datacenter . so that is is useful to generate current qos easily
	
	public void createVms(int numOfVmsToBeCreated){
		numOfAvailableVms = numOfAvailableVms - numOfVmsToBeCreated;
	}
	
	/**
	 * @return the id of the datacenter
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id of the datacenter to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the numOfAvailableVms
	 */
	public int getNumOfAvailableVms() {
		return numOfAvailableVms;
	}

	/**
	 * @param numOfAvailableVms the numOfAvailableVms to set
	 */
	public void setNumOfAvailableVms(int numOfAvailableVms) {
		this.numOfAvailableVms = numOfAvailableVms;
	}
	
	
	
}
