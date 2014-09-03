package org.sit.cloud.marketplace.decision;

import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.GeoLocation;
import org.sit.cloud.marketplace.entities.ProviderParams;

public class RoundRobinProviderSelector extends ProviderSelector {

	@Override
	protected List<ProviderParams> performInitialFiltering(
			List<ProviderParams> providers, int numOfVms) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Integer> getAllocationMapAfterInitialFiltering(
			GeoLocation geoLocation, List<ProviderParams> providers,
			int numOfVms) {
		// TODO Auto-generated method stub
		return null;
	}

}
