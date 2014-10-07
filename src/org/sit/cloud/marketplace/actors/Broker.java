package org.sit.cloud.marketplace.actors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		registry = new Registry();
		providerSelector = new FuzzyProviderSelector();
		vmIdToNumberOfPollsMap = new HashMap<String, Integer>();
	}
	
	public void registerProvider(Provider provider){
		registry.registerProvider(provider);
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
	
	public void acceptUserRequest(UserRequest userRequest){
		int numOfVms = userRequest.getNumOfVms();
		List<ProviderParams> providerParams = registry.getProviderParams();
		Map<String, Integer> allocationMap = providerSelector.selectBestProvider(providerParams, numOfVms, userRequest);
		for(String providerId : allocationMap.keySet()){
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
				
				bandwidthSatisfactionMap.put(vm.getId(), 1.0);
				availabilitySatisfactionMap.put(vm.getId(), 1.0);
				
				vmIdToNumberOfPollsMap.put(vm.getId(), 0);
			}
		}
	}

	
	public void performMonitoringAndMigrations() throws IOException{
		Map<String, QoS> vmIdToQosMap = monitorVms();
		Map<String, SlaViolationData> vmIdToSlaViolationMap = calculateSlaViolations(vmIdToQosMap);
		checkForSLAViolation(vmIdToSlaViolationMap);
	}
	
	/**
	 * This function fetched the currently offered QoS for each VM from the provider hosting it.
	 * The data for all the VMs is appended in a single map and returned.
	 * If the user wants to get the QoS data of a VM printed, the printing is done here. 
	 * @return a map from VM to the QoS experienced by that VM
	 * @throws IOException
	 */
	public Map<String, QoS> monitorVms() throws IOException{
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
	
	public Map<String, SlaViolationData> calculateSlaViolations(Map<String, QoS> vmIdToQosMap){
		Map<String, SlaViolationData> vmIdToSlaViolationMap = new HashMap<String, SlaViolationData>();
		for(String vmId : vmIdToQosMap.keySet()){
			Transaction transaction = registry.getVmIdToTransactionMap().get(vmId);
			vmIdToSlaViolationMap.put(vmId, new SlaViolationData(transaction.getAvailability(), vmIdToQosMap.get(vmId).getAvailability(), transaction.getBw(), vmIdToQosMap.get(vmId).getBandwidth()));
		}
		return vmIdToSlaViolationMap;
	}
	
	public void checkForSLAViolation(Map<String, SlaViolationData> vmIdToSlaViolationMap){
		for(String vmId : vmIdToSlaViolationMap.keySet()){
			
			//ADDING THE EXPERIENCED QoS PARAMETERS SO THAT AVERAGE CAN BE CALCULATED AT THE END OF THE WEEK
			sumOfExperiencedAvailabilityMap.put(vmId, sumOfExperiencedAvailabilityMap.get(vmId) + vmIdToSlaViolationMap.get(vmId).getExperiencedAvailability());
			sumOfExperiencedBandwidthMap.put(vmId, sumOfExperiencedBandwidthMap.get(vmId) + vmIdToSlaViolationMap.get(vmId).getExperiencedBandwidth());
			vmIdToNumberOfPollsMap.put(vmId, vmIdToNumberOfPollsMap.get(vmId)+1); // INCREMENTING THE NUMBER OF POLLS DONE BY 1
			
			if(TimeKeeper.shouldViolationsBeCalculated()){
				// NOW CALCULATE F_A(t_i) and F_BW(t_i)
				availabilitySatisfactionMap.put(vmId, (sumOfExperiencedAvailabilityMap.get(vmId)/(vmIdToNumberOfPollsMap.get(vmId))) + (availabilitySatisfactionMap.get(vmId)/Math.E));
				bandwidthSatisfactionMap.put(vmId, (sumOfExperiencedBandwidthMap.get(vmId)/(vmIdToNumberOfPollsMap.get(vmId))) + (bandwidthSatisfactionMap.get(vmId)/Math.E));
				
				// RESETING THE WEEK'S CALCULATION AFTER THE CALCULATION OF THE SATISFACTION VALUES
				sumOfExperiencedAvailabilityMap.put(vmId, 0.0);
				sumOfExperiencedBandwidthMap.put(vmId, 0.0);
				vmIdToNumberOfPollsMap.put(vmId, 0);
			}
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
}