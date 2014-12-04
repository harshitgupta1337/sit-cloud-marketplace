package org.sit.cloud.marketplace.actors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import migrdecider.migrdecider;
import migrdecidernew.migrdecidernew;

import org.sit.cloud.marketplace.decision.CrispProviderSelector;
import org.sit.cloud.marketplace.decision.FuzzyProviderSelector;
import org.sit.cloud.marketplace.decision.MigrationDecisionMaker;
import org.sit.cloud.marketplace.decision.ProviderSelector;
import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.QoS;
import org.sit.cloud.marketplace.entities.SlaViolationData;
import org.sit.cloud.marketplace.entities.Transaction;
import org.sit.cloud.marketplace.entities.UserRequest;
import org.sit.cloud.marketplace.entities.Vm;
import org.sit.cloud.marketplace.utils.TimeKeeper;
import org.sit.cloud.marketplace.utils.Utils;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;


public class Broker {
	
	private static double MIGRATION_THRESHOLD = 0.85;
	
	private static double ALPHA = 1/Math.E;
	
	private Registry registry;
	private ProviderSelector providerSelector;
	private ProviderSelector crispSelector;
	private MigrationDecisionMaker migrationDecisionMaker;
	private Map<String, Double> vmIdToMigrationValue;
	
	/**
	 * A handle which is used to switch the broker between fuzzy and crisp modes
	 */
	private boolean isCrisp;
	
	/**
	 * A handle which switches between migration and no-migration when in the fuzzy mode of the broker
	 */
	private boolean isMigrationNeeded;

	/**
	 * A map that maps each Vm id to the sum of the experienced availability for an entire week
	 */
	private Map<String, Double> sumOfExperiencedAvailabilityMap;

	/**
	 * A map that maps each Vm id to the sum of the experienced bandwidth for an entire week
	 */
	private Map<String, Double> sumOfExperiencedBandwidthMap;
	
	/**
	 * A map that maps each Vm id to the satisfaction in availability for that Vm
	 */
	private Map<String, Double> bandwidthSatisfactionMap;
	
	/**
	 * A map that maps each Vm id to the satisfaction in bandwidth for that Vm
	 */
	private Map<String, Double> availabilitySatisfactionMap;
	
	/**
	 * A map that maps each PROVIDER to the value of it's TRUST in AVAILABILITY
	 */
	private Map<String, Double> availabilityTrustMap;
	
	/**
	 * A map that maps each PROVIDER to the value of it's TRUST in BANDWIDTH
	 */
	private Map<String, Double> bandwidthTrustMap;
	
	/**
	 * A map that maps every VM to the number of times it's experienced QoS has been polled by the broker. 
	 * This value is reset every week after the satisfaction values are calculated.
	 * This value also needs to be reset after the VM is migrated onto a different provider. 
	 */
	private Map<String, Integer> vmIdToNumberOfPollsMap;
	
	/**
	 * Map storing the total cost incurred for running the VM on the respective user
	 */
	private Map<String, Double> vmIdToCostMap;
	private Map<String, Double> vmIdToBwMap;
	private Map<String, Double> vmIdToAvailMap;
	
	private Map<String, Integer> vmIdToAvgCostDenomMap;
	
	private Map<String, UserRequest> vmIdToUserRequestMap;
	
	private Map<String, UserRequest> userReqIdToUserReqMap;
	private Map<String, String> userReqIdToVmIdMap;
	
	/**
	 * List of ids of VMs whose experienced QoS parameters have to be printed and plotted
	 */
	private List<String> vmsToBePlotted;
	private Map<String, BufferedWriter> writerForVm;
	//private migrdecider theMigration;
	private migrdecidernew theMigration;
	
	public Broker(){
		userReqIdToUserReqMap = new HashMap<String, UserRequest>();
		userReqIdToVmIdMap = new HashMap<String, String>();
		vmIdToUserRequestMap = new HashMap<String, UserRequest>();
		vmIdToCostMap = new HashMap<String, Double>();
		vmIdToAvgCostDenomMap = new HashMap<String, Integer>();
		vmIdToMigrationValue = new HashMap<String, Double>();
		crispSelector = new CrispProviderSelector();
		registry = new Registry();
		providerSelector = new FuzzyProviderSelector();
		vmIdToNumberOfPollsMap = new HashMap<String, Integer>();
		availabilitySatisfactionMap = new HashMap<String, Double>();
		bandwidthSatisfactionMap = new HashMap<String, Double>();
		vmsToBePlotted = new ArrayList<String>();
		writerForVm = new HashMap<String, BufferedWriter>();
		availabilityTrustMap = new HashMap<String, Double>();
		bandwidthTrustMap = new HashMap<String, Double>();
		sumOfExperiencedAvailabilityMap = new HashMap<String, Double>();
		sumOfExperiencedBandwidthMap = new HashMap<String, Double>();
		migrationDecisionMaker = new MigrationDecisionMaker();
		vmIdToAvailMap = new HashMap<String, Double>();
		vmIdToBwMap = new HashMap<String, Double>();
		isCrisp = false;
		isMigrationNeeded = false;
		try {
			//theMigration = new migrdecider();
			theMigration = new migrdecidernew();
		} catch (MWException e) {
			e.printStackTrace();
		}
	}
	
	public void registerProvider(Provider provider){
		registry.registerProvider(provider);
		availabilityTrustMap.put(provider.getId(), 1.5);
		bandwidthTrustMap.put(provider.getId(), 1.5);
	}

	public void registerUser(User user){
		registry.registerUser(user);
	}

	private void registerVmWithUser(Vm vm, String userId){
		registry.registerVmWithUser(vm, userId);
	}
	
	private ProviderParams getProviderParamsForProviderId(List<ProviderParams> providerParams, String providerId){
		for(ProviderParams proviParams : providerParams){
			if(proviParams.getProviderId().equals(providerId))
				return proviParams;
		}
		return null;
	}
	
	private void insertTrustValuesInProviderParams(List<ProviderParams> providerParams){
		for(ProviderParams providerParam : providerParams){
			providerParam.setTrustInAvailability(availabilityTrustMap.get(providerParam.getProviderId()));
			providerParam.setTrustInBandwidth(bandwidthTrustMap.get(providerParam.getProviderId()));
		}
	}
	
	public List<ProviderParams> getProviderParams(){
		List<ProviderParams> params = registry.getProviderParams();
		insertTrustValuesInProviderParams(params);
		return params;
	}
	
	public List<ProviderParams> getCurrentExperiencedProviderParams(){
		List<ProviderParams> params = registry.getCurrentExperiencedProviderParams();
		insertTrustValuesInProviderParams(params);
		return params;
	}
	
	public void acceptUserRequest(UserRequest userRequest){
		userReqIdToUserReqMap.put(userRequest.getId(), userRequest);
		int numOfVms = userRequest.getNumOfVms();
		List<ProviderParams> providerParams = getProviderParams();
		Map<String, Integer> allocationMap;
		
		if(isCrisp)
			allocationMap = crispSelector.selectBestProvider(providerParams, numOfVms, userRequest, false);
		else
			allocationMap = providerSelector.selectBestProvider(providerParams, numOfVms, userRequest, false);
		for(String providerId : allocationMap.keySet()){
			//System.out.println(registry.getProviderIdtoProviderMap().get(providerId).getPromisedAvailability()+"\t"+registry.getProviderIdtoProviderMap().get(providerId).getPromisedBandwidth()+"\t"+registry.getProviderIdtoProviderMap().get(providerId).getCost());
			//System.out.println("------------------------------------\n");
			//System.out.println("VMs available : "+registry.getProviderIdtoProviderMap().get(providerId).getNumOfAvailableVms());
			for(int i=0;i<allocationMap.get(providerId);i++){
				//
				// SOME VERY INTRICATE THINGS NEED TO BE DONE HERE 
				
				Vm vm = new Vm(userRequest.isShouldBeViolated());
				vm.setProviderId(providerId);
				registerVmWithUser(vm, userRequest.getUserId());
				ProviderParams promisedParams = getProviderParamsForProviderId(providerParams, providerId);
				Transaction transaction = new Transaction(userRequest.getUserId(), vm.getId(), promisedParams.getAvailability(), promisedParams.getCost(), promisedParams.getBw(), userRequest.getRequiredAvailability(), userRequest.getRequiredBandwidth(), userRequest.getMaxAffordableCost());
				registry.getVmIdToTransactionMap().put(vm.getId(), transaction);
				vmIdToMigrationValue.put(vm.getId(), 1.0);
				vmIdToAvgCostDenomMap.put(vm.getId(), 0);
				vmIdToCostMap.put(vm.getId(), 0.0);
				vmIdToAvailMap.put(vm.getId(), 0.0);
				vmIdToBwMap.put(vm.getId(), 0.0);
				userReqIdToVmIdMap.put(userRequest.getId(), vm.getId());
				// SENDING THE CREATED VM TO THE PROVIDER
				registry.getProviderIdtoProviderMap().get(providerId).createVm(vm);
				//System.out.println("VM ID "+vm.getId()+" created in Provider ID "+providerId);
				sumOfExperiencedAvailabilityMap.put(vm.getId(), 0.0);
				sumOfExperiencedBandwidthMap.put(vm.getId(), 0.0);
				
				bandwidthSatisfactionMap.put(vm.getId(), 1.5);
				availabilitySatisfactionMap.put(vm.getId(), 1.5);
				
				vmIdToNumberOfPollsMap.put(vm.getId(), 0);
				
				vmIdToUserRequestMap.put(vm.getId(), userRequest);
			}
		}
	}

	
	public void performMonitoringAndMigrations() throws IOException, MWException{
		Map<String, QoS> vmIdToQosMap = monitorVms();
		Map<String, SlaViolationData> vmIdToSlaViolationMap = calculateSlaViolations(vmIdToQosMap);
		checkForSLAViolation(vmIdToSlaViolationMap);
				
		if(TimeKeeper.shouldViolationsBeCalculated()){
			/*for(String vmId : registry.getProviderIdtoProviderMap().get("1").getRunningVmIdToVmMap().keySet()){
				System.out.println("Value for VM ID "+vmId+" = "+vmIdToMigrationValue.get(vmId));
				System.out.println("AVAIL F value for VM ID "+vmId+" = "+availabilitySatisfactionMap.get(vmId));
				System.out.println("BANDWIDTH F value for VM ID "+vmId+" = "+bandwidthSatisfactionMap.get(vmId));
			}*/

			evaluateCosts(vmIdToSlaViolationMap);
			performNecessaryMigrations();
		}
	}
	
	private void evaluateCosts(Map<String, SlaViolationData> slaViolMap){
		for(String vmId : vmIdToCostMap.keySet()){
			vmIdToCostMap.put(vmId, vmIdToCostMap.get(vmId)+registry.getVmIdToTransactionMap().get(vmId).getCost());
			
			vmIdToAvailMap.put(vmId, vmIdToAvailMap.get(vmId)+slaViolMap.get(vmId).getExperiencedAvailability());
			vmIdToBwMap.put(vmId, vmIdToBwMap.get(vmId)+slaViolMap.get(vmId).getExperiencedBandwidth());
			
			vmIdToAvgCostDenomMap.put(vmId, vmIdToAvgCostDenomMap.get(vmId)+1);
		}
	}
	
	private void performNecessaryMigrations() throws MWException{
		if(vmIdToMigrationValue.keySet().size() > 0){
			double satisfactionValues[][] = new double[vmIdToMigrationValue.keySet().size()][2];
			int i=0;
			
			for(String vmId : vmIdToMigrationValue.keySet()){
				satisfactionValues[i][0] = availabilitySatisfactionMap.get(vmId);
				if(satisfactionValues[i][0] > 1.6){
					System.out.println("AVAIL OUT OF RANGE");
					System.exit(0);
				}
				satisfactionValues[i][1] = bandwidthSatisfactionMap.get(vmId);
				if(satisfactionValues[i][1] > 1.6){
					System.out.println("BANDWIDTH OUT OF RANGE");
					System.exit(0);
				}
				i++;
			}
			Object[] y = null;
			MWNumericArray x = new MWNumericArray(satisfactionValues, MWClassID.DOUBLE);
			//System.out.println(x.toArray().length);
			//y=theMigration.migration(1,x);
			y=theMigration.migration_shrink(1,x);
			
			String[] parts = y[0].toString().split("\n");
		    i=0;
			for(String vmId : vmIdToMigrationValue.keySet()){
				vmIdToMigrationValue.put(vmId, Double.parseDouble(parts[i]));
				i++;
			}
		}	
		
		printAverageMigrationValue();
		
		for(String vmId : registry.getVmIdToVmMap().keySet()){
			if(doesVmNeedMigration(vmId)){
				//System.out.println("VM ID "+vmId+" needs migration.");
				migrateVm(vmId);
			}
		}
	}
	
	
	private List<ProviderParams> selectPossibleProviders(List<ProviderParams> providerParams, double requestedAvailability, double requestedBandwidth, double currentProviderAvailTrust, double currentProviderBwTrust){
		List<ProviderParams> suitableProviders = new ArrayList<ProviderParams>();
		
		for(ProviderParams param : providerParams){
			if(param.getAvailability() >= requestedAvailability && param.getBw()>=requestedBandwidth && 
					availabilityTrustMap.get(param.getProviderId())>=currentProviderAvailTrust && 
					bandwidthTrustMap.get(param.getProviderId())>=currentProviderBwTrust){
				param.setTrustInAvailability(availabilityTrustMap.get(param.getProviderId()));
				param.setTrustInBandwidth(bandwidthTrustMap.get(param.getProviderId()));
				suitableProviders.add(param);
			}
		}
		return suitableProviders;
	}
	
	private void migrateVm(String vmId) throws MWException{
		String currentProviderId = registry.getVmIdToVmMap().get(vmId).getProviderId();
		
		List<ProviderParams> possibleTargetProviders = selectPossibleProviders(getProviderParams(), 
				registry.getVmIdToTransactionMap().get(vmId).getRequestedAvailability(), 
				registry.getVmIdToTransactionMap().get(vmId).getRequestedBandwidth(), 
				availabilityTrustMap.get(currentProviderId), 
				bandwidthTrustMap.get(currentProviderId));
		//System.out.println("POSSIBLE PROVIDERS = "+possibleTargetProviders.size());
		if(possibleTargetProviders.size() == 1){
			if(possibleTargetProviders.get(0).getProviderId() == currentProviderId){
				//System.out.println("Case 1");
				return;
			}
		}
		
		String providerId = migrationDecisionMaker.selectTargetProviderForMigration(currentProviderId, availabilityTrustMap.get(currentProviderId), 
				bandwidthTrustMap.get(currentProviderId), registry.getProviderIdtoProviderMap().get(currentProviderId).getPromisedAvailability(),
				registry.getProviderIdtoProviderMap().get(currentProviderId).getPromisedBandwidth(), registry.getProviderIdtoProviderMap().get(currentProviderId).getCost(), 
				possibleTargetProviders, getProviderParams(), vmIdToUserRequestMap.get(vmId));
		//System.out.println("Current provider ID : "+currentProviderId);
		//System.out.println("New provider ID : "+providerId);
		if(providerId != null && providerId != currentProviderId)
			migrateVm(registry.getVmIdToVmMap().get(vmId).getProviderId(), providerId, vmId);
		//else
			//System.out.println("PROVIDER WAS NULL");
	}
	
	
	/**
	 * This function fetched the currently offered QoS for each VM from the provider hosting it.
	 * The data for all the VMs is appended in a single map and returned.
	 * If the user wants to get the QoS data of a VM printed, the printing is done here. 
	 * @return a map from VM to the QoS experienced by that VM
	 * @throws IOException
	 */
	private Map<String, QoS> monitorVms() throws IOException{
		Map<String, QoS> vmIdToQosMap = new HashMap<String, QoS>();
		for(String providerId : registry.getProviderIdtoProviderMap().keySet()){
			Map<String, QoS> vmIdToQosMapForProvider = registry.getProviderIdtoProviderMap().get(providerId).getQosExperiencedByVms();
			for(String vmId : vmIdToQosMapForProvider.keySet()){
				vmIdToQosMap.put(vmId, vmIdToQosMapForProvider.get(vmId));
				if(writerForVm.containsKey(vmId)){
					writerForVm.get(vmId).write(vmIdToQosMapForProvider.get(vmId).getAvailability() + "\t" + vmIdToQosMapForProvider.get(vmId).getBandwidth() + "\n");
				}
			}
		}
		return vmIdToQosMap;
	}
	
	private Map<String, SlaViolationData> calculateSlaViolations(Map<String, QoS> vmIdToQosMap){
		Map<String, SlaViolationData> vmIdToSlaViolationMap = new HashMap<String, SlaViolationData>();
		for(String vmId : vmIdToQosMap.keySet()){
			Transaction transaction = registry.getVmIdToTransactionMap().get(vmId);
			vmIdToSlaViolationMap.put(vmId, new SlaViolationData(transaction.getAvailability(), vmIdToQosMap.get(vmId).getAvailability(), transaction.getBw(), vmIdToQosMap.get(vmId).getBandwidth()));
		}
		return vmIdToSlaViolationMap;
	}
	
	private void checkForSLAViolation(Map<String, SlaViolationData> vmIdToSlaViolationMap){
		
		for(String vmId : vmIdToSlaViolationMap.keySet()){
			//ADDING THE EXPERIENCED QoS PARAMETERS SO THAT AVERAGE CAN BE CALCULATED AT THE END OF THE WEEK
			sumOfExperiencedAvailabilityMap.put(vmId, sumOfExperiencedAvailabilityMap.get(vmId) + vmIdToSlaViolationMap.get(vmId).getExperiencedAvailability());
			sumOfExperiencedBandwidthMap.put(vmId, sumOfExperiencedBandwidthMap.get(vmId) + vmIdToSlaViolationMap.get(vmId).getExperiencedBandwidth());
			vmIdToNumberOfPollsMap.put(vmId, vmIdToNumberOfPollsMap.get(vmId)+1); // INCREMENTING THE NUMBER OF POLLS DONE BY 1
		}
		if(TimeKeeper.shouldViolationsBeCalculated()){
			//printAverageGValue(vmIdToSlaViolationMap);
			for(String vmId : vmIdToSlaViolationMap.keySet()){
				// NOW CALCULATE F_A(t_i) and F_BW(t_i)
				availabilitySatisfactionMap.put(vmId, Math.min(sumOfExperiencedAvailabilityMap.get(vmId)/
						(vmIdToNumberOfPollsMap.get(vmId) * vmIdToSlaViolationMap.get(vmId).getPromisedAvailability()) 
						+ availabilitySatisfactionMap.get(vmId)*ALPHA, 1.5));
				bandwidthSatisfactionMap.put(vmId, Math.min(sumOfExperiencedBandwidthMap.get(vmId)/
						(vmIdToNumberOfPollsMap.get(vmId) * vmIdToSlaViolationMap.get(vmId).getPromisedBandwidth()) 
						+ bandwidthSatisfactionMap.get(vmId)*ALPHA, 1.5));
				
				refreshTrustValues();	
				
				// RESETING THE WEEK'S CALCULATION AFTER THE CALCULATION OF THE SATISFACTION VALUES
				sumOfExperiencedAvailabilityMap.put(vmId, 0.0);
				sumOfExperiencedBandwidthMap.put(vmId, 0.0);
				vmIdToNumberOfPollsMap.put(vmId, 0);
			}
		}
		
	}
	
	private void printAverageMigrationValue(){
		double total = 0;
		for(String vmId : vmIdToMigrationValue.keySet()){
			total += vmIdToMigrationValue.get(vmId);
		}
		if(vmIdToMigrationValue.keySet().size()>0)
			System.out.println(total/vmIdToMigrationValue.keySet().size());
	}
	
	private void printAverageGValue(Map<String, SlaViolationData> vmIdToSlaViolationMap){
		double sumGAvail = 0.0;
		for(String vmId : sumOfExperiencedAvailabilityMap.keySet()){
			sumGAvail += (sumOfExperiencedAvailabilityMap.get(vmId))/(vmIdToNumberOfPollsMap.get(vmId) * vmIdToSlaViolationMap.get(vmId).getPromisedAvailability()) ;
		}
		double sumGBw = 0.0;
		for(String vmId : sumOfExperiencedBandwidthMap.keySet()){
			sumGBw += (sumOfExperiencedBandwidthMap.get(vmId))/(vmIdToNumberOfPollsMap.get(vmId) * vmIdToSlaViolationMap.get(vmId).getPromisedBandwidth()) ;
		}
		System.out.println((sumGAvail/sumOfExperiencedAvailabilityMap.keySet().size())+"\t"+(sumGBw/sumOfExperiencedBandwidthMap.keySet().size()));
	}
	
	/**
	 * This function refreshes the trust values for each provider.
	 * It computes the trust as the average of the satisfaction values of all the VMs running on a provider.
	 */
	private void refreshTrustValues(){
		Map<String, List<String>> providerIdToVmIdMap = new HashMap<String, List<String>>();
		for(String providerId : registry.getProviderIdtoProviderMap().keySet()){
			providerIdToVmIdMap.put(providerId, new ArrayList<String>());
		}
		for(String vmId : registry.getVmIdToVmMap().keySet()){
			if(providerIdToVmIdMap.get(registry.getVmIdToVmMap().get(vmId).getProviderId())==null)
				providerIdToVmIdMap.put(registry.getVmIdToVmMap().get(vmId).getProviderId(), new ArrayList<String>());
			providerIdToVmIdMap.get(registry.getVmIdToVmMap().get(vmId).getProviderId()).add(vmId);
		}
		for(String providerId : availabilityTrustMap.keySet()){
			double totalSatisfactionValue = 0;
			for(String vmId : providerIdToVmIdMap.get(providerId)){
				totalSatisfactionValue += availabilitySatisfactionMap.get(vmId);
			}
			if(providerIdToVmIdMap.get(providerId).size() > 0)
				availabilityTrustMap.put(providerId, totalSatisfactionValue/providerIdToVmIdMap.get(providerId).size());			
		}
		for(String providerId : bandwidthTrustMap.keySet()){
			double totalSatisfactionValue = 0;
			for(String vmId : providerIdToVmIdMap.get(providerId)){
				totalSatisfactionValue += bandwidthSatisfactionMap.get(vmId);
			}
			if(providerIdToVmIdMap.get(providerId).size() > 0)
				bandwidthTrustMap.put(providerId, totalSatisfactionValue/providerIdToVmIdMap.get(providerId).size());			
		}
	}
	
	/**
	 * 
	 * This function would apply the fuzzy logic controller on the degree of SLA violation experienced by this VM 
	 * and then decide whether it needs migration or not
	 * 
	 * @param vmId
	 * @return true if the vm requires to be migrated , false otherwise
	 */
	private boolean doesVmNeedMigration(String vmId){
		if(isCrisp)
			return false;
		if(!isMigrationNeeded)
			return false;
		if(vmIdToMigrationValue.get(vmId) < MIGRATION_THRESHOLD)
			return true;
		else
			return false;
	}
	
	public void migrateVm(String sourceProvider, String destProvider, String vmId){
		//System.out.println("Migrating vm ID "+ vmId +" from provider ID "+sourceProvider+" to providerID "+destProvider);
		registry.getProviderIdtoProviderMap().get(sourceProvider).getRunningVmIdToVmMap().remove(vmId);
		registry.getVmIdToVmMap().get(vmId).setProviderId(destProvider);
		registry.getProviderIdtoProviderMap().get(destProvider).createVm(registry.getVmIdToVmMap().get(vmId));
		
		Transaction oldTransaction = registry.getVmIdToTransactionMap().get(vmId);
		ProviderParams newParams = registry.getProviderIdtoProviderMap().get(destProvider).getPromisedQos();
		
		registry.getVmIdToTransactionMap().put(vmId, new Transaction(oldTransaction.getUserId(), vmId, newParams.getAvailability(), newParams.getCost(), newParams.getBw(), oldTransaction.getRequestedAvailability(), oldTransaction.getRequestedBandwidth(), oldTransaction.getMaxAffordableCost()));
		
		bandwidthSatisfactionMap.put(vmId, 1.5);
		availabilitySatisfactionMap.put(vmId, 1.5);
	}
	
	public void printAverageCost(){
		System.out.println("VM ID\tREQ COST\tAVG COST\tREQ AVAIL\tAVG AVAIL\tREQ BW\tAVG BW");
		for(String vmId : vmIdToCostMap.keySet()){
			if(vmIdToAvgCostDenomMap.get(vmId) > 0){
				System.out.println(vmId + "\t" + registry.getVmIdToTransactionMap().get(vmId).getMaxAffordableCost() + "\t" + 
						+ vmIdToCostMap.get(vmId)/vmIdToAvgCostDenomMap.get(vmId) + "\t" + registry.getVmIdToTransactionMap().get(vmId).getRequestedAvailability() + "\t"+
						vmIdToAvailMap.get(vmId)/vmIdToAvgCostDenomMap.get(vmId)+ "\t" + registry.getVmIdToTransactionMap().get(vmId).getRequestedBandwidth() + "\t"+
								vmIdToBwMap.get(vmId)/vmIdToAvgCostDenomMap.get(vmId));
			}
		}
	}
	
	public void printSimulationDetailsWrtUserReq(){
		System.out.println("USERREQ ID\tREQ COST\tAVG COST\tREQ AVAIL\tAVG AVAIL\tREQ BW\tAVG BW");
		for(String userReqId : userReqIdToUserReqMap.keySet()){
			if(userReqIdToVmIdMap.containsKey(userReqId)){
				String vmId = userReqIdToVmIdMap.get(userReqId);
				if(vmIdToAvgCostDenomMap.get(vmId) > 0){
					System.out.println(userReqId + "\t" + registry.getVmIdToTransactionMap().get(vmId).getMaxAffordableCost() + "\t" + 
							+ vmIdToCostMap.get(vmId)/vmIdToAvgCostDenomMap.get(vmId) + "\t" + registry.getVmIdToTransactionMap().get(vmId).getRequestedAvailability() + "\t"+
							vmIdToAvailMap.get(vmId)/vmIdToAvgCostDenomMap.get(vmId)+ "\t" + registry.getVmIdToTransactionMap().get(vmId).getRequestedBandwidth() + "\t"+
									vmIdToBwMap.get(vmId)/vmIdToAvgCostDenomMap.get(vmId));
				}
			}else{
				System.out.println(userReqId + "\t" + userReqIdToUserReqMap.get(userReqId).getMaxAffordableCost() + "\t" + 
						"\t"  + userReqIdToUserReqMap.get(userReqId).getRequiredAvailability() + "\t"+
						"\t" + userReqIdToUserReqMap.get(userReqId).getRequiredBandwidth() + "\t"+
								"\t");
			}
		}
	}
	
	
	public Registry getRegistry() {
		return registry;
	}
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public ProviderSelector getProviderSelector() {
		return providerSelector;
	}

	public void setProviderSelector(ProviderSelector providerSelector) {
		this.providerSelector = providerSelector;
	}

	public Map<String, Double> getbandwidthSatisfactionMap() {
		return bandwidthSatisfactionMap;
	}

	public void setbandwidthSatisfactionMap(Map<String, Double> bandwidthSatisfactionMap) {
		this.bandwidthSatisfactionMap = bandwidthSatisfactionMap;
	}

	public Map<String, Double> getAvailabilitySatisfactionMap() {
		return availabilitySatisfactionMap;
	}

	public void setAvailabilitySatisfactionMap(
			Map<String, Double> availabilitySatisfactionMap) {
		this.availabilitySatisfactionMap = availabilitySatisfactionMap;
	}


	public List<String> getVmsToBePlotted() {
		return vmsToBePlotted;
	}


	public void setVmsToBePlotted(List<String> vmsToBePlotted) throws IOException {
		this.vmsToBePlotted = vmsToBePlotted;
		for(String vmId : vmsToBePlotted){
			writerForVm.put(vmId, new BufferedWriter(new FileWriter(new File(Utils.OUTPUT_FOLDER_LOCATION + vmId).getAbsoluteFile())));
		}
	}

	public Map<String, BufferedWriter> getWriterForVm() {
		return writerForVm;
	}

	public void setWriterForVm(Map<String, BufferedWriter> writerForVm) {
		this.writerForVm = writerForVm;
	}

	/**
	 * @return the vmIdToNumberOfPollsMap
	 */
	public Map<String, Integer> getVmIdToNumberOfPollsMap() {
		return vmIdToNumberOfPollsMap;
	}

	/**
	 * @param vmIdToNumberOfPollsMap the vmIdToNumberOfPollsMap to set
	 */
	public void setVmIdToNumberOfPollsMap(Map<String, Integer> vmIdToNumberOfPollsMap) {
		this.vmIdToNumberOfPollsMap = vmIdToNumberOfPollsMap;
	}

	public Map<String, Double> getAvailabilityTrustMap() {
		return availabilityTrustMap;
	}

	public void setAvailabilityTrustMap(Map<String, Double> availabilityTrustMap) {
		this.availabilityTrustMap = availabilityTrustMap;
	}

	public Map<String, Double> getBandwidthTrustMap() {
		return bandwidthTrustMap;
	}

	public void setBandwidthTrustMap(Map<String, Double> bandwidthTrustMap) {
		this.bandwidthTrustMap = bandwidthTrustMap;
	}

	public Map<String, Double> getVmIdToMigrationValue() {
		return vmIdToMigrationValue;
	}

	public void setVmIdToMigrationValue(Map<String, Double> vmIdToMigrationValue) {
		this.vmIdToMigrationValue = vmIdToMigrationValue;
	}

	public Map<String, Double> getVmIdToCostMap() {
		return vmIdToCostMap;
	}

	public void setVmIdToCostMap(Map<String, Double> vmIdToCostMap) {
		this.vmIdToCostMap = vmIdToCostMap;
	}

	public Map<String, Integer> getVmIdToAvgCostDenomMap() {
		return vmIdToAvgCostDenomMap;
	}

	public void setVmIdToAvgCostDenomMap(Map<String, Integer> vmIdToAvgCostDenomMap) {
		this.vmIdToAvgCostDenomMap = vmIdToAvgCostDenomMap;
	}

	public Map<String, UserRequest> getVmIdToUserRequestMap() {
		return vmIdToUserRequestMap;
	}

	public void setVmIdToUserRequestMap(Map<String, UserRequest> vmIdToUserRequestMap) {
		this.vmIdToUserRequestMap = vmIdToUserRequestMap;
	}

	public Map<String, UserRequest> getUserReqIdToUserReqMap() {
		return userReqIdToUserReqMap;
	}

	public void setUserReqIdToUserReqMap(Map<String, UserRequest> userReqIdToUserReqMap) {
		this.userReqIdToUserReqMap = userReqIdToUserReqMap;
	}

	public Map<String, String> getUserReqIdToVmIdMap() {
		return userReqIdToVmIdMap;
	}

	public void setUserReqIdToVmIdMap(Map<String, String> userReqIdToVmIdMap) {
		this.userReqIdToVmIdMap = userReqIdToVmIdMap;
	}

	public boolean isMigrationNeeded() {
		return isMigrationNeeded;
	}

	public void setMigrationNeeded(boolean isMigrationNeeded) {
		this.isMigrationNeeded = isMigrationNeeded;
	}	
}