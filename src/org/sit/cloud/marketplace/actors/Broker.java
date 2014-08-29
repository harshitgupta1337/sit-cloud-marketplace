package org.sit.cloud.marketplace.actors;

import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.decision.ProviderSelector;
import org.sit.cloud.marketplace.entities.GeoLocation;
import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.Transaction;
import org.sit.cloud.marketplace.entities.UserRequest;
import org.sit.cloud.marketplace.entities.Vm;


public class Broker {
	
	private Registry registry;
	private ProviderSelector providerSelector;
	private List<Transaction> transactions;
	
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
	
	private ProviderParams getProviderParamsForProviderId(List<ProviderParams> providerParams, String providerId){
		for(ProviderParams proviParams : providerParams){
			if(proviParams.getProviderId().equals(providerId))
				return proviParams;
		}
		return null;
	}
	
	public void acceptUserRequest(UserRequest userRequest){
		List<ProviderParams> providerParams = registry.getProviderParams();
		Map<GeoLocation, Integer> geoLocationToNumOfVmsMap = userRequest.getGeoLocationToNumOfVmsMap();
		for(GeoLocation geoLocation : geoLocationToNumOfVmsMap.keySet()){
			int numOfVms = geoLocationToNumOfVmsMap.get(geoLocation);
			Map<String, Integer> allocationMap = providerSelector.selectBestProvider(geoLocation, providerParams, numOfVms);
			for(String providerId : allocationMap.keySet()){
				for(int i=0;i<allocationMap.get(providerId);i++){
					//
					// SOME VERY INTRICATE THINGS NEED TO BE DONE HERE 
					
					Vm vm = new Vm();
					registerVmWithUser(vm, userRequest.getUserId());
					ProviderParams promisedParams = getProviderParamsForProviderId(providerParams, providerId);
					Transaction transaction = new Transaction(userRequest.getUserId(), vm.getId(), promisedParams.getAvailability(), promisedParams.getCost(), promisedParams.getBw());
					registry.getVmIdToTransactionMap().put(vm.getId(), transaction);
					registry.getProviderIdtoProviderMap().get(providerId).sendVmToGeoLocation(vm, geoLocation);
					
					//
				}
			}
		}
	}
	
	public void monitorVms(){
		for(String providerId : registry.getProviderIdtoProviderMap().keySet()){
			registry.getProviderIdtoProviderMap().get(providerId).getQosExperiencedByVms();
		}
	}
	public void checkForSLAViolation(){
		for(String vmId : registry.getVmIdToVmMap().keySet()){
			Vm vm = registry.getVmIdToVmMap().get(vmId);
			double experience = vm.getExperience();
		}
	}
	
	public void migrateVm(String sourceProvider, String destProvider, String vmId){
		
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
