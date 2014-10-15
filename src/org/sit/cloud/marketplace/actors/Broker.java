package org.sit.cloud.marketplace.actors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.decision.CrispProviderSelector;
import org.sit.cloud.marketplace.decision.FuzzyProviderSelector;
import org.sit.cloud.marketplace.decision.ProviderSelector;
import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.QoS;
import org.sit.cloud.marketplace.entities.SlaViolationData;
import org.sit.cloud.marketplace.entities.Transaction;
import org.sit.cloud.marketplace.entities.UserRequest;
import org.sit.cloud.marketplace.entities.Vm;
import org.sit.cloud.marketplace.utils.TimeKeeper;
import org.sit.cloud.marketplace.utils.Utils;


public class Broker {
	
	private Registry registry;
	private ProviderSelector providerSelector;
	private ProviderSelector crispSelector;

	/**
	 * A map that maps each Vm id to the sum of the experienced availability for an entire week
	 */
	private Map<String, Double> sumOfExperiencedAvailabilityMap;

	/**
	 * A map that maps each Vm id to the sum of the experienced bandwidth for an entire week
	 */
	private Map<String, Double> sumOfExperiencedBandwidthMap;
	
	/**
	 * A map that maps each Vm id to the sum of the satisfaction in availability for that Vm
	 */
	private Map<String, Double> bandwidthSatisfactionMap;
	
	/**
	 * A map that maps each Vm id to the sum of the satisfaction in bandwidth for that Vm
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
	 * List of ids of VMs whose experienced QoS parameters have to be printed and plotted
	 */
	private List<String> vmsToBePlotted;
	private Map<String, BufferedWriter> writerForVm;
	
	public Broker(){
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
	
	public void acceptUserRequest(UserRequest userRequest){
		int numOfVms = userRequest.getNumOfVms();
		List<ProviderParams> providerParams = registry.getProviderParams();
		insertTrustValuesInProviderParams(providerParams);
		System.out.println("YES");
		Map<String, Integer> allocationMap = providerSelector.selectBestProvider(providerParams, numOfVms, userRequest);
		System.out.println(allocationMap.keySet().size());
		for(String providerId : allocationMap.keySet()){
			System.out.println("Provider ID : "+providerId + " VMs : "+allocationMap.get(providerId));
			System.out.println("VMs available : "+registry.getProviderIdtoProviderMap().get(providerId).getNumOfAvailableVms());
			for(int i=0;i<allocationMap.get(providerId);i++){
				//
				// SOME VERY INTRICATE THINGS NEED TO BE DONE HERE 
				
				Vm vm = new Vm(userRequest.isShouldBeViolated());
				registerVmWithUser(vm, userRequest.getUserId());
				ProviderParams promisedParams = getProviderParamsForProviderId(providerParams, providerId);
				Transaction transaction = new Transaction(userRequest.getUserId(), vm.getId(), promisedParams.getAvailability(), promisedParams.getCost(), promisedParams.getBw());
				registry.getVmIdToTransactionMap().put(vm.getId(), transaction);
				
				// SENDING THE CREATED VM TO THE PROVIDER
				registry.getProviderIdtoProviderMap().get(providerId).createVm(vm);
				
				sumOfExperiencedAvailabilityMap.put(vm.getId(), 0.0);
				sumOfExperiencedBandwidthMap.put(vm.getId(), 0.0);
				
				bandwidthSatisfactionMap.put(vm.getId(), 1.5);
				availabilitySatisfactionMap.put(vm.getId(), 1.5);
				
				vmIdToNumberOfPollsMap.put(vm.getId(), 0);
			}
		}
	}

	
	public void performMonitoringAndMigrations() throws IOException{
		Map<String, QoS> vmIdToQosMap = monitorVms();
		Map<String, SlaViolationData> vmIdToSlaViolationMap = calculateSlaViolations(vmIdToQosMap);
		checkForSLAViolation(vmIdToSlaViolationMap);
		performNecessaryMigrations();
	}
	
	private void performNecessaryMigrations(){
		for(String vmId : registry.getVmIdToVmMap().keySet()){
			if(doesVmNeedMigration(vmId)){
				migrateVm(vmId);
			}
		}
	}
	
	private void migrateVm(String vmId){
		
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
			
			if(TimeKeeper.shouldViolationsBeCalculated()){
				// NOW CALCULATE F_A(t_i) and F_BW(t_i)
				availabilitySatisfactionMap.put(vmId, (sumOfExperiencedAvailabilityMap.get(vmId)/(vmIdToNumberOfPollsMap.get(vmId))) + (availabilitySatisfactionMap.get(vmId)/Math.E));
				bandwidthSatisfactionMap.put(vmId, (sumOfExperiencedBandwidthMap.get(vmId)/(vmIdToNumberOfPollsMap.get(vmId))) + (bandwidthSatisfactionMap.get(vmId)/Math.E));
				
				refreshTrustValues();
				
				// RESETING THE WEEK'S CALCULATION AFTER THE CALCULATION OF THE SATISFACTION VALUES
				sumOfExperiencedAvailabilityMap.put(vmId, 0.0);
				sumOfExperiencedBandwidthMap.put(vmId, 0.0);
				vmIdToNumberOfPollsMap.put(vmId, 0);
			}
		}
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
		return false;
	}
	
	public void migrateVm(String sourceProvider, String destProvider, String vmId){
		
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
}