package org.sit.cloud.marketplace.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.Transaction;
import org.sit.cloud.marketplace.entities.Vm;

public class Registry {
	
	private Map<String, User> userIdtoUserMap;
	
	private Map<String, Boolean> vmIdToIsMarkedMap;
	
	private Map<String, Provider> providerIdtoProviderMap;
	
	private Map<String, List<String>> userIdToVmIdsMap;
	
	private Map<String, Vm> vmIdToVmMap;
	
	private Map<String, Transaction> vmIdToTransactionMap;
	
	public Registry(){
		providerIdtoProviderMap = new HashMap<String, Provider>();
		setVmIdToTransactionMap(new HashMap<String, Transaction>());
		userIdtoUserMap = new HashMap<String, User>();
		vmIdToVmMap = new HashMap<String, Vm>();
		userIdToVmIdsMap = new HashMap<String, List<String>>();
		vmIdToIsMarkedMap = new HashMap<String, Boolean>();
	}
	
	public void registerUser(User user){
		userIdtoUserMap.put(user.getId(), user);
		userIdToVmIdsMap.put(user.getId(), new ArrayList<String>());
	}

	public void registerProvider(Provider provider){
		providerIdtoProviderMap.put(provider.getId(), provider);
	}
	
	public List<ProviderParams> getProviderParams(){
		List<ProviderParams> providerParamsList = new ArrayList<ProviderParams>();
		for(String providerId : providerIdtoProviderMap.keySet()){
			ProviderParams params = providerIdtoProviderMap.get(providerId).getPromisedQos();
			if(params != null)
				providerParamsList.add(params);
		}
		return providerParamsList;
	}
	public void registerVmWithUser(Vm vm, String userId){
		userIdToVmIdsMap.get(userId).add(vm.getId());
		vmIdToVmMap.put(vm.getId(), vm);	
	}
	
	public List<ProviderParams> getCurrentExperiencedProviderParams(){
		List<ProviderParams> providerParamsList = new ArrayList<ProviderParams>();
		for(String providerId : providerIdtoProviderMap.keySet()){
			ProviderParams params = providerIdtoProviderMap.get(providerId).getExperiencedProviderParams();
			if(params != null)
				providerParamsList.add(params);
		}
		return providerParamsList;
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

	public Map<String, List<String>> getUserIdToVmIdsMap() {
		return userIdToVmIdsMap;
	}

	public void setUserIdToVmIdsMap(Map<String, List<String>> userIdToVmIdsMap) {
		this.userIdToVmIdsMap = userIdToVmIdsMap;
	}

	public Map<String, Vm> getVmIdToVmMap() {
		return vmIdToVmMap;
	}

	public void setVmIdToVmMap(Map<String, Vm> vmIdToVmMap) {
		this.vmIdToVmMap = vmIdToVmMap;
	}

	public Map<String, User> getUserIdtoUserMap() {
		return userIdtoUserMap;
	}

	public void setUserIdtoUserMap(Map<String, User> userIdtoUserMap) {
		this.userIdtoUserMap = userIdtoUserMap;
	}

	public Map<String, Transaction> getVmIdToTransactionMap() {
		return vmIdToTransactionMap;
	}

	public void setVmIdToTransactionMap(Map<String, Transaction> vmIdToTransactionMap) {
		this.vmIdToTransactionMap = vmIdToTransactionMap;
	}
}
