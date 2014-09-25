package org.sit.cloud.marketplace.actors;

import java.util.Map;
import java.util.UUID;

import org.sit.cloud.marketplace.entities.Datacenter;
import org.sit.cloud.marketplace.entities.GeoLocation;
import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.QoS;
import org.sit.cloud.marketplace.entities.Vm;

public class Provider {
	
	String id;
	private int cores;
	private int ram;
	private int storage;
	private double cost;
	private double promisedAvailability;
	private double promisedBandwidth;
	
	
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

	private Map<GeoLocation, Datacenter> geoLocationToDatacenterMap;
	
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
	 * @return the geoLocationToDatacenterMap
	 */
	public Map<GeoLocation, Datacenter> getGeoLocationToDatacenterMap() {
		return geoLocationToDatacenterMap;
	}

	/**
	 * @param geoLocationToDatacenterMap the geoLocationToDatacenterMap to set
	 */
	public void setGeoLocationToDatacenterMap(
			Map<GeoLocation, Datacenter> geoLocationToDatacenterMap) {
		this.geoLocationToDatacenterMap = geoLocationToDatacenterMap;
	}

	public Provider(){
		id = UUID.randomUUID().toString();
	}
	
	/**
	 * This function returns the QoS details promised by the provider. It is currently independent of the GeoLocation
	 * except, of course the number of available VMs.
	 * @return
	 */
	public ProviderParams getPromisedQos(GeoLocation geoLocation){
		ProviderParams params = new ProviderParams(id, getPromisedAvailability(), getCost(), getPromisedBandwidth(),  cores, ram, storage);
		params.setNumOfVmsAvailable(geoLocationToDatacenterMap.get(geoLocation).getNumOfAvailableVms());
		return params;
	}
	
	public ProviderParams getCurrentQos(GeoLocation geolocation){
		// HERE WE SHOULD APPLY SOME CURVES TO CHANGE THE QOS ACCORDING TO THE SCENARIO TO BE MODELED
		
		if(!geoLocationToDatacenterMap.containsKey(geolocation))
			return null;
		Datacenter dc = geoLocationToDatacenterMap.get(geolocation);
		if(dc == null)
			return null;
		ProviderParams params = new ProviderParams(id, dc.getCurrentAvailability(), getCost(), dc.getCurrentBandwidth(),  cores, ram, storage);
		params.setNumOfVmsAvailable(geoLocationToDatacenterMap.get(geolocation).getNumOfAvailableVms());
		return params;
	}
	
	public void sendVmToGeoLocation(Vm vm, GeoLocation geoLocation){
		Datacenter datacenter = geoLocationToDatacenterMap.get(geoLocation);
		datacenter.createVm(vm);
		
	}
	
	public Map<String, QoS> getQosExperiencedByVms(){
		return null;
	}
	
	public boolean addDatacenter(GeoLocation geoLocation, Datacenter datacenter){
		if(geoLocationToDatacenterMap.containsKey(geoLocation))
			return false;
		geoLocationToDatacenterMap.put(geoLocation, datacenter);
		return true;
	}
	
	public double getAvailability(){
		return 0.0;
	}
	public double getCost(){
		return 0.0;
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
}
