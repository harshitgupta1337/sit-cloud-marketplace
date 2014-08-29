package org.sit.cloud.marketplace.entities;

import java.util.UUID;

public class Vm {

	private String id;
	private String providerId;
	
	public Vm(){
		id = UUID.randomUUID().toString();
		providerId = null;
	}
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
