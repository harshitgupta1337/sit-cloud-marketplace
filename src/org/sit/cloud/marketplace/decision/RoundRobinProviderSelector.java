package org.sit.cloud.marketplace.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;

public class RoundRobinProviderSelector extends ProviderSelector {

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

	private List<ProviderParams> sortProviderParams(List<ProviderParams> providerParams){
		for(int i=0;i<providerParams.size();i++){
			ProviderParams smallestCost = providerParams.get(i);
			int smallestCostIndex = i;
			for(int j=i;j<providerParams.size();j++){
				if(providerParams.get(j).getCost() < smallestCost.getCost()){
					smallestCost = providerParams.get(j);
					smallestCostIndex = j;
				}
			}
			ProviderParams provider_i = providerParams.get(i); 
			providerParams.remove(i);
			providerParams.add(i, smallestCost);
			providerParams.remove(smallestCostIndex);
			providerParams.add(smallestCostIndex, provider_i);
			
		}
		return providerParams;
	}

	@Override
	protected Map<String, Integer> getAllocationMapAfterInitialFiltering(
			List<ProviderParams> providerParams, int numOfVms) {
		Map<String, Integer> allocationMap = new HashMap<String, Integer>();
		List<ProviderParams> sortedProviderParams = sortProviderParams(providerParams);
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

}
