package org.sit.cloud.marketplace.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.UserRequest;

public class CrispProviderSelector extends ProviderSelector {

	@Override
	protected List<ProviderParams> performInitialFiltering(
			List<ProviderParams> providers, int cores, int ram, int storage, int numOfVms) {
		List<ProviderParams> filteredProviders = new ArrayList<ProviderParams>();
		for(ProviderParams providerParams : providers){
			if(providerParams.getCores() >= cores && providerParams.getStorage() >= storage && providerParams.getRam() >= ram && providerParams.getNumOfVmsAvailable() >= numOfVms)
				filteredProviders.add(providerParams);
		}
		//System.out.println("Filtered providers' size : "+filteredProviders.size());
		return filteredProviders;
	}

	private List<ProviderParams> sortProviderParams(List<ProviderParams> providerParams){
		//System.out.println("Sorted providers' size : "+providerParams.size());

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

	protected List<ProviderParams> getSuitableProviders(List<ProviderParams> params, UserRequest userRequest){
		//System.out.println("Suitable providers' size : "+params.size());
		List<ProviderParams> suitableProviders = new ArrayList<ProviderParams>();
		//System.out.println("Bandwidth : "+userRequest.getRequiredBandwidth());
		//System.out.println("Availability : "+userRequest.getRequiredAvailability());
		//System.out.println("Cost : "+userRequest.getMaxAffordableCost());
		
		for(ProviderParams providerParam : params){
			
			if(providerParam.getBw() >= userRequest.getRequiredBandwidth() && providerParam.getAvailability() >= userRequest.getRequiredAvailability() && providerParam.getCost()<=userRequest.getMaxAffordableCost()){
				suitableProviders.add(providerParam);
				//System.out.println("YES111");
			}
				
		}
		return suitableProviders;
	}
	
	@Override
	protected Map<String, Integer> getAllocationMapAfterInitialFiltering(
			List<ProviderParams> providerParams, UserRequest userRequest, boolean toPrint) {
		Map<String, Integer> allocationMap = new HashMap<String, Integer>();
		List<ProviderParams> sortedProviderParams = sortProviderParams(getSuitableProviders(providerParams, userRequest));
		int remainingVmsToBeAllocated = userRequest.getNumOfVms();
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
