package org.sit.cloud.marketplace.actors;

import java.util.UUID;

import org.sit.cloud.marketplace.entities.ProviderParams;

public class Provider {
	
	String id;

	public Provider(){
		id = UUID.randomUUID().toString();
	}
	
	public ProviderParams getCurrentQos(){
		// HERE WE SHOULD APPLY SOME CURVES TO CHANGE THE QOS ACCORDING TO THE SCENARIO TO BE MODELED
		return new ProviderParams();
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
