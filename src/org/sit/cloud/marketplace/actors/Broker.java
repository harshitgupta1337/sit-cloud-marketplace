package org.sit.cloud.marketplace.actors;

import java.util.Map;

import org.sit.cloud.marketplace.decision.ProviderSelector;
import org.sit.cloud.marketplace.entities.GeoLocation;
import org.sit.cloud.marketplace.entities.UserRequest;
import org.sit.cloud.marketplace.entities.Vm;


public class Broker {
	
	private Registry registry;
	private ProviderSelector providerSelector;
	public Broker(){
		registry = new Registry();
	}

	public void registerProvider(Provider provider){
		registry.registerProvider(provider);
	}

	public void registerUser(User user){
		registry.registerUser(user);
	}

	private void registerVmWithUser(Vm vm, String userId){
		registry.registerVmWithUser(vm, userId);
	}
	
	public void acceptUserRequest(UserRequest userRequest){
		Map<GeoLocation, Integer> geoLocationToNumOfVmsMap = userRequest.getGeoLocationToNumOfVmsMap();
		for(GeoLocation geoLocation : geoLocationToNumOfVmsMap.keySet()){
			int numOfVms = geoLocationToNumOfVmsMap.get(geoLocation);
			Map<String, Integer> allocationMap = providerSelector.selectBestProvider(geoLocation, registry.getProviderParams(), numOfVms);
			for(String providerId : allocationMap.keySet()){
				for(int i=0;i<allocationMap.get(providerId);i++){
					//
					// SOME VERY INTRICATE THINGS NEED TO BE DONE HERE 
					
					Vm vm = new Vm();
					registerVmWithUser(vm, userRequest.getUserId());
					registry.getProviderIdtoProviderMap().get(providerId).sendVmToGeoLocation(vm, geoLocation);
					
					//
				}
			}
		}
	}
	
	/**
	 * @return the registry
	 */
	public Registry getRegistry() {
		return registry;
	}

	/**
	 * @param registry the registry to set
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public ProviderSelector getProviderSelector() {
		return providerSelector;
	}

	public void setProviderSelector(ProviderSelector providerSelector) {
		this.providerSelector = providerSelector;
	}

	
}
