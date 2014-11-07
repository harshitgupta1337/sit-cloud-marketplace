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
	
		//Provider pool
		//First provider is the current provider
		//First provider values are promised_avail,promised_bw,promised_cost,current_trust_avail,current_trust_bw
		//Rest providers are prospective new providers
		//Rest providers values are offered_avail,offered_bw,offered_cost,current_trust_avail,current_trust_bw
		/*double[][] input = new double[params.size()+1][5];
		input[0][0] = currentAvail;
		input[0][1] = currentBw;
		input[0][2] = currentCost;
		input[0][3] = currentAvailTrust;
		input[0][4] = currentBwTrust;
		//System.out.println("Prospective target providers = "+params.size());
		for(int i=0;i<params.size();i++){
			input[i+1][0] = params.get(i).getAvailability();
			input[i+1][1] = params.get(i).getBw();
			input[i+1][2] = params.get(i).getCost();
			input[i+1][3] = params.get(i).getTrustInAvailability();
			input[i+1][4] = params.get(i).getTrustInBandwidth();
		}
		MWNumericArray x = new MWNumericArray(input, MWClassID.DOUBLE);
		//MWNumericArray idx = new MWNumericArray(index, MWClassID.DOUBLE);
		//y=thecalclust.calhierclust(1,x,idx);
		y=thecalclust.calhierclust(1,x);
		//System.out.println("The output is  = \n" + y[0].toString());
		int number = Integer.parseInt(y[0].toString());
		
		// If the output is 1, that means there is no provider found suitable.
		// otherwise the output is index of the provider from above provider pool
		if(number == 1)
			return null;
		int index=0;
		/*for(ProviderParams param : params){
			System.out.println(index++ + "\t" + param.getAvailability()+"\t"+param.getBw()+"\t"+param.getCost());
		}
		System.out.println("Chose index : " + (number-2));
		*/
		//System.out.format("The selected provider's ID is =  " + params.get(number-2).getProviderId());
		/*System.out.println();
		return params.get(number-2).getProviderId();*/
	}
}
