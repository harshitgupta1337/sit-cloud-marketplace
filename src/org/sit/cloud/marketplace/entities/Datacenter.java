package org.sit.cloud.marketplace.entities;

import java.util.Map;

public class Datacenter {

	private String id;
	private int numOfAvailableVms;
	private Map<String, Vm> runningVmIdToVmMap;
	private double currentAvailability;
	private double currentBandwidth;
	
	public void createVms(int numOfVmsToBeCreated){
		numOfAvailableVms = numOfAvailableVms - numOfVmsToBeCreated;
	}
	
	public void createVm(Vm vm){
		numOfAvailableVms--;
		runningVmIdToVmMap.put(vm.getId(), vm);
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

	public double getCurrentAvailability() {
		return currentAvailability;
	}

	public void setCurrentAvailability(double currentAvailability) {
		this.currentAvailability = currentAvailability;
	}

	public double getCurrentBandwidth() {
		return currentBandwidth;
	}

	public void setCurrentBandwidth(double currentBandwidth) {
		this.currentBandwidth = currentBandwidth;
	}
}
