package org.sit.cloud.marketplace.actors;

import java.util.HashMap;
import java.util.Map;

public class Registry {
	
	private Map<String, Provider> providerIdtoProviderMap;
	
	public Registry(){
		setProviderIdtoProviderMap(new HashMap<String, Provider>());
	}

	public void registerProvider(Provider provider){
		providerIdtoProviderMap.put(provider.getId(), provider);
	}
	
	/**
	 * @return the providerIdtoProviderMap
	 */
	public Map<String, Provider> getProviderIdtoProviderMap() {
		return providerIdtoProviderMap;
	}

	/**
	 * @param providerIdtoProviderMap the providerIdtoProviderMap to set
	 */
	public void setProviderIdtoProviderMap(Map<String, Provider> providerIdtoProviderMap) {
		this.providerIdtoProviderMap = providerIdtoProviderMap;
	}
}
