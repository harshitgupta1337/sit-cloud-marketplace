package org.sit.cloud.marketplace.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;

public class FuzzyProviderSelector extends ProviderSelector {
	@Override
	protected List<ProviderParams> performInitialFiltering(
			List<ProviderParams> providers, int cores, int ram, int storage) {
		List<ProviderParams> filteredProviders = new ArrayList<ProviderParams>();
		for(ProviderParams providerParams : providers){
			if(providerParams.getCores() >= cores && providerParams.getStorage() >= storage && providerParams.getRam() >= ram)
				filteredProviders.add(providerParams);
		}
		return filteredProviders;	
	}

	@Override
	protected Map<String, Integer> getAllocationMapAfterInitialFiltering(
			List<ProviderParams> providerParams, int numOfVms) {
		Map<String, Integer> allocationMap = new HashMap<String, Integer>();
		List<ProviderParams> sortedProviderParams = sortProvidersFuzzily(providerParams);
		int remainingVmsToBeAllocated = numOfVms;
		for(int i=0;i<sortedProviderParams.size();i++){
			if(remainingVmsToBeAllocated == 0)
				break;
			int numOfVmsAvailable = sortedProviderParams.get(i).getNumOfVmsAvailable();
			if(numOfVmsAvailable >= remainingVmsToBeAllocated){
				allocationMap.put(sortedProviderParams.get(i).getProviderId(), remainingVmsToBeAllocated);
				remainingVmsToBeAllocated = 0;
			}else{
				allocationMap.put(sortedProviderParams.get(i).getProviderId(), numOfVmsAvailable);
				remainingVmsToBeAllocated -= numOfVmsAvailable;
			}
		}
		return allocationMap;
	}

	/**
	 * This function sorts the providers' parameters based on their fuzzy utility.
	 * @param providerParams
	 * @return list of providers' parameters sorted based on their fuzzy utility
	 */
	private List<ProviderParams> sortProvidersFuzzily(List<ProviderParams> providerParams){
			for(ProviderParams providerParam : providerParams){
				setFuzzyValueForProvider(providerParam);
			}
			for(int i=0;i<providerParams.size();i++){
				ProviderParams largestUtilityProvider = providerParams.get(i);
				int largestUtilityIndex = i;
				for(int j=i;j<providerParams.size();j++){
					if(providerParams.get(j).getFuzzyUtility() > largestUtilityProvider.getFuzzyUtility()){
						largestUtilityProvider = providerParams.get(j);
						largestUtilityIndex = j;
					}
				}
				ProviderParams provider_i = providerParams.get(i); 
				providerParams.remove(i);
				providerParams.add(i, largestUtilityProvider);
				providerParams.remove(largestUtilityIndex);
				providerParams.add(largestUtilityIndex, provider_i);
			}
			return providerParams;
	}
	
	/**
	 * This function calculates the fuzzy utility (v -value) for a provider's parameters.
	 * The function makes a call the MATLAB code to use the FIS.
	 * @param providerParams
	 */
	private void setFuzzyValueForProvider(ProviderParams providerParams){
		/*
		 * Here, we need to set the Fuzzy V value for the provider. @sujeet you need to put your code here.
		 */
		providerParams.setFuzzyUtility(0);
	}
}
