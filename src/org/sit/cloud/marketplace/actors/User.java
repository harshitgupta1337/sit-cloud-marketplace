package org.sit.cloud.marketplace.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	
	
	
}
