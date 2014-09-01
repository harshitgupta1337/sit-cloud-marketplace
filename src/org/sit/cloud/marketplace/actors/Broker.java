package org.sit.cloud.marketplace.actors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.decision.ProviderSelector;
import org.sit.cloud.marketplace.entities.GeoLocation;
import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.QoS;
import org.sit.cloud.marketplace.entities.SlaViolationData;
import org.sit.cloud.marketplace.entities.Transaction;
import org.sit.cloud.marketplace.entities.UserRequest;
import org.sit.cloud.marketplace.entities.Utils;
import org.sit.cloud.marketplace.entities.Vm;
import org.sit.cloud.marketplace.utils.TimeKeeper;


public class Broker {
	
	private Registry registry;
	private ProviderSelector providerSelector;
	private Map<String, Double> sumOfExperiencedAvailabilityMap;
	private Map<String, Double> sumOfExperiencedBandwidthMap;
	private Map<String, Double> bandwidthSatisfactionMap;
	private Map<String, Double> availabilitySatisfactionMap;
	
	/**
	 * List of ids of VMs whose experienced QoS parameters have to be printed and plotted
	 */
	private List<String> vmsToBePlotted;
	private Map<String, BufferedWriter> writerForVm;
	
	public Broker(){
		registry = new Registry();
		providerSelector = new ProviderSelector();
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

		Map<GeoLocation, Integer> geoLocationToNumOfVmsMap = userRequest.getGeoLocationToNumOfVmsMap();
		for(GeoLocation geoLocation : geoLocationToNumOfVmsMap.keySet()){
			List<ProviderParams> providerParams = registry.getProviderParams(geoLocation);
			int numOfVms = geoLocationToNumOfVmsMap.get(geoLocation);
			Map<String, Integer> allocationMap = providerSelector.selectBestProvider(geoLocation, providerParams, numOfVms);
			for(String providerId : allocationMap.keySet()){
				for(int i=0;i<allocationMap.get(providerId);i++){
					//
					// SOME VERY INTRICATE THINGS NEED TO BE DONE HERE 
					
					Vm vm = new Vm(userRequest.isShouldBeViolated());
					registerVmWithUser(vm, userRequest.getUserId());
					ProviderParams promisedParams = getProviderParamsForProviderId(providerParams, providerId);
					Transaction transaction = new Transaction(userRequest.getUserId(), vm.getId(), promisedParams.getAvailability(), promisedParams.getCost(), promisedParams.getBw());
					registry.getVmIdToTransactionMap().put(vm.getId(), transaction);
					registry.getProviderIdtoProviderMap().get(providerId).sendVmToGeoLocation(vm, geoLocation);
					sumOfExperiencedAvailabilityMap.put(vm.getId(), 0.0);
					sumOfExperiencedBandwidthMap.put(vm.getId(), 0.0);
					bandwidthSatisfactionMap.put(vm.getId(), 0.0);
					availabilitySatisfactionMap.put(vm.getId(), 0.0);
				}
			}
		}
	}
	
	public void performMonitoringAndMigrations() throws IOException{
		Map<String, QoS> vmIdToQosMap = monitorVms();
		Map<String, SlaViolationData> vmIdToSlaViolationMap = calculateSlaViolations(vmIdToQosMap);
		checkForSLAViolation(vmIdToSlaViolationMap);
	}
	
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
			if(TimeKeeper.shouldViolationsBeCalculated()){
				// NOW CALCULATE F_A(t_i) and F_BW(t_i)
				availabilitySatisfactionMap.put(vmId, (sumOfExperiencedAvailabilityMap.get(vmId)/(24*7)) + (availabilitySatisfactionMap.get(vmId)/Math.E));
				bandwidthSatisfactionMap.put(vmId, (sumOfExperiencedBandwidthMap.get(vmId)/(24*7)) + (bandwidthSatisfactionMap.get(vmId)/Math.E));
				
				// RESETING THE WEEK'S CALCULATION
				sumOfExperiencedAvailabilityMap.put(vmId, 0.0);
				sumOfExperiencedBandwidthMap.put(vmId, 0.0);
			}else{
				//ADDING THE EXPERIENCED QoS PARAMETERS SO THAT AVERAGE CAN BE CALCULATED AT THE END OF THE WEEK
				sumOfExperiencedAvailabilityMap.put(vmId, sumOfExperiencedAvailabilityMap.get(vmId) + vmIdToSlaViolationMap.get(vmId).getExperiencedAvailability());
				sumOfExperiencedBandwidthMap.put(vmId, sumOfExperiencedBandwidthMap.get(vmId) + vmIdToSlaViolationMap.get(vmId).getExperiencedBandwidth());
			}
		}
	}
	
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
}