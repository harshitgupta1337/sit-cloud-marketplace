package org.sit.cloud.marketplace.decision;

import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.UserRequest;

public abstract class ProviderSelector {
	
	public Map<String, Integer> selectBestProvider(List<ProviderParams> providers, int numOfVms, UserRequest userRequest){
		return getAllocationMapAfterInitialFiltering(performInitialFiltering(providers, userRequest.getCores(), userRequest.getRam(), userRequest.getStorage()), numOfVms);
	}
	
	protected abstract List<ProviderParams> performInitialFiltering(List<ProviderParams> providers, int cores, int ram, int storage);
	
	protected abstract Map<String, Integer> getAllocationMapAfterInitialFiltering(List<ProviderParams> providers, int numOfVms);
	
}
