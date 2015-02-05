package org.sit.cloud.marketplace.decision;

import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.UserRequest;

import calhierclust.calclust;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class MigrationDecisionMaker {

	
	
	public String selectTargetProviderForMigration(String currentProviderId, double currentAvailTrust, double currentBwTrust, double currentAvail, double currentBw, double currentCost, List<ProviderParams> params, List<ProviderParams> allProviders, UserRequest userRequest) throws MWException{
		//System.out.println("Inside selectTargetProviderForMigration. Current prooviderId = " + currentProviderId);
		calclust thecalclust=new calclust();
		Object[] y = null;
		/*int del = -1;
		for(int i=0;i<params.size();i++){
			if(params.get(i).getProviderId().equals(currentProviderId))
				del = i;
		}
		if(del != -1)
			params.remove(del);
		*/

		FuzzyProviderSelector selector = new FuzzyProviderSelector();
			
		selector.setFuzzyValueForProviders(userRequest, allProviders, false);
			
		double currentV = 0.0;
		for(ProviderParams providerParams : allProviders){	
			if(providerParams.getProviderId().equals(currentProviderId)){
				currentV = providerParams.getFuzzyUtility();
			}
		}
		double highestVExcludingCurrent = currentV-0.05; String high = null;
		for(ProviderParams providerParams : allProviders){	
			if(!providerParams.getProviderId().equals(currentProviderId)){
				if(providerParams.getFuzzyUtility() >= highestVExcludingCurrent){
					highestVExcludingCurrent = providerParams.getFuzzyUtility();
					high = providerParams.getProviderId();
				}
			}
		}
		if(high != null)
			return high;
		else
			return currentProviderId;
	}
}
