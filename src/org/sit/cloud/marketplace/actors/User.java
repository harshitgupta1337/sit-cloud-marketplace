package org.sit.cloud.marketplace.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.sit.cloud.marketplace.entities.UserRequest;

public class User {

	String id;
	List<String> vmIds;
	public User(){
		id = UUID.randomUUID().toString();
		vmIds = new ArrayList<String>();
	}
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getVmIds() {
		return vmIds;
	}
	public void setVmIds(List<String> vmIds) {
		this.vmIds = vmIds;
	}
	
	/**
	 * Given the no. of vms, this function randomly splits it into various geolocations
	 * and creates a user request
	 * @param numOfVms
	 * @return a randomly generated user request
	 */
	public UserRequest createuserRequest(int numOfVms, double requiredAvailability, double requiredBandwidth, int cores, int ram, int storage, double maxAffordableCost, boolean shouldBeViolated){
		return new UserRequest(getId(), numOfVms,  requiredAvailability, requiredBandwidth, cores, ram, storage, maxAffordableCost, shouldBeViolated);
	}
	
	
}
