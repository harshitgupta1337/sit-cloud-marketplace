package org.sit.cloud.marketplace.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.UserRequest;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import fuzzyopt1.fuzopt1;

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
			List<ProviderParams> providerParams, UserRequest userRequest) {
		Map<String, Integer> allocationMap = new HashMap<String, Integer>();
		List<ProviderParams> sortedProviderParams = sortProvidersFuzzily(providerParams, userRequest);
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

	/**
	 * This function sorts the providers' parameters based on their fuzzy utility.
	 * @param providerParams
	 * @return list of providers' parameters sorted based on their fuzzy utility
	 */
	private List<ProviderParams> sortProvidersFuzzily(List<ProviderParams> providerParams, UserRequest userRequest){
			for(ProviderParams providerParam : providerParams){
				try {
					setFuzzyValueForProvider(userRequest, providerParam);
				} catch (MWException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	private void setFuzzyValueForProvider(UserRequest userRequest, ProviderParams providerParams) throws MWException{
		/*
		 * Here, we need to set the Fuzzy V value for the provider. @sujeet you need to put your code here.
		 */
		fuzopt1 thefuzopt=new fuzopt1();
		Object[] y=null;
		//First three numbers are provider commitments : 97.5=P1 availability,97.8=P1's bw in mbps,97=P1's cost per VM per Hr
		//Next two numbers are providers trust_avail and trust_bw.These values are given by broker
		//Next two numbers are Cust reqd availability and da (da is reqd for availbility MF)
		//Next two numbers are Cust reqd BW and db (db is reqd for BW MF)
		//Next two numbers are Cust reqd cost and dc (dc is reqd for cost MF)
		//Next last number is do (do is reqd for output MF)
		//double[] AData = {98,98,97,1.5,1.5,98,0.5,98,0.5,97,0.5,0.334};
		double[] AData = {providerParams.getAvailability(), providerParams.getBw(), providerParams.getCost(), 
				1.6, 1.6, userRequest.getRequiredAvailability(), 1, 
				userRequest.getRequiredBandwidth(), 1.5, userRequest.getMaxAffordableCost(), 10, 0.334};

		MWNumericArray x = new MWNumericArray(AData, MWClassID.DOUBLE);
		y=thefuzopt.dynafis2(1, x);
		System.out.println("The output is  = \n" + y[0].toString());
		
		providerParams.setFuzzyUtility(Float.parseFloat(y[0].toString()));
	}
}
