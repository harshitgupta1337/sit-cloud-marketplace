package org.sit.cloud.marketplace.decision;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.GeoLocation;
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

	@Override
	protected Map<String, Integer> getAllocationMapAfterInitialFiltering(
			GeoLocation geoLocation, List<ProviderParams> providers,
			int numOfVms) {
		
		return null;
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

}
