package org.sit.cloud.marketplace.actors;

import java.util.Map;
import java.util.UUID;

import org.sit.cloud.marketplace.entities.Datacenter;
import org.sit.cloud.marketplace.entities.GeoLocation;
import org.sit.cloud.marketplace.entities.ProviderParams;

public class Provider {
	
	String id;
	private int cores;
	private int ram;
	private int storage;
	
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

	Map<GeoLocation, Datacenter> geoLocationToDatacenterMap;

	public Provider(){
		id = UUID.randomUUID().toString();
	}
	
	public ProviderParams getCurrentQos(long time){
		// HERE WE SHOULD APPLY SOME CURVES TO CHANGE THE QOS ACCORDING TO THE SCENARIO TO BE MODELED
		ProviderParams params = new ProviderParams(id, getAvailability(time), getCost(time), getBandwidth(time),  cores, ram, storage);
		for(GeoLocation geolocation : geoLocationToDatacenterMap.keySet()){
			params.getAvailableVmsMap().put(geolocation, geoLocationToDatacenterMap.get(geolocation).getNumOfAvailableVms());
		}
		return params;
	}
	
	public double getAvailability(long time){
		return 0.0;
	}
	public double getCost(long time){
		return 0.0;
	}
	public double getBandwidth(long time){
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
}
