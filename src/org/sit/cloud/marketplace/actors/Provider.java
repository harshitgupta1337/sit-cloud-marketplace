package org.sit.cloud.marketplace.actors;

import java.util.Map;
import java.util.UUID;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.QoS;
import org.sit.cloud.marketplace.entities.Vm;

public class Provider {
	
	private String id;
	private int cores;
	private int ram;
	private int storage;
	private double cost;
	private double promisedAvailability;
	private double promisedBandwidth;
	private int numOfAvailableVms;
	private Map<String, Vm> runningVmIdToVmMap;
	private double currentAvailability;
	private double currentBandwidth;
	
	public Provider(String id, int cores, int ram, int storage, double cost, double promisedAvailability, double promisedBandwidth, int vmCapacity){
		this.id = id;
		this.cores = cores;
		this.ram = ram;
		this.storage = storage;
		this.cost = cost;
		this.promisedAvailability = promisedAvailability;
		this.promisedBandwidth = promisedBandwidth;
		this.numOfAvailableVms = vmCapacity;
	}
	public int getNumOfAvailableVms(){
		return numOfAvailableVms;
	}
	
	public void setNumOfAvailableVms(int numOfAvailableVms){
		this.numOfAvailableVms = numOfAvailableVms;
	}
	
	public void createVms(int numOfVmsToBeCreated){
		numOfAvailableVms = numOfAvailableVms - numOfVmsToBeCreated;
	}
	
	public void createVm(Vm vm){
		numOfAvailableVms--;
		runningVmIdToVmMap.put(vm.getId(), vm);
	}
	
	/**
	 * @return the promisedAvailability
	 */
	public double getPromisedAvailability() {
		return promisedAvailability;
	}

	/**
	 * @param promisedAvailability the promisedAvailability to set
	 */
	public void setPromisedAvailability(double promisedAvailability) {
		this.promisedAvailability = promisedAvailability;
	}

	/**
	 * @return the promisedBandwidth
	 */
	public double getPromisedBandwidth() {
		return promisedBandwidth;
	}

	/**
	 * @param promisedBandwidth the promisedBandwidth to set
	 */
	public void setPromisedBandwidth(double promisedBandwidth) {
		this.promisedBandwidth = promisedBandwidth;
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

	public Provider(){
		id = UUID.randomUUID().toString();
	}
	
	/**
	 * This function returns the QoS details promised by the provider. It is currently independent of the GeoLocation
	 * except, of course the number of available VMs.
	 * @return
	 */
	public ProviderParams getPromisedQos(){
		ProviderParams params = new ProviderParams(id, getPromisedAvailability(), getCost(), getPromisedBandwidth(),  cores, ram, storage);
		params.setNumOfVmsAvailable(getNumOfAvailableVms());
		return params;
	}
	
	public ProviderParams getCurrentQos(){
		// HERE WE SHOULD APPLY SOME CURVES TO CHANGE THE QOS ACCORDING TO THE SCENARIO TO BE MODELED
		
		ProviderParams params = new ProviderParams(id, getCurrentAvailability(), getCost(), getCurrentBandwidth(),  cores, ram, storage);
		params.setNumOfVmsAvailable(getNumOfAvailableVms());
		return params;
	}
	
	public Map<String, QoS> getQosExperiencedByVms(){
		
		// IF ACTUAL QoS > PROMISED QoS, THEN RETURN PROMISED VALUE
		
		return null;
	}
	
	public double getAvailability(){
		return 0.0;
	}
	public double getCost(){
		return cost;
	}
	public double getBandwidth(){
		return 0.0;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
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
