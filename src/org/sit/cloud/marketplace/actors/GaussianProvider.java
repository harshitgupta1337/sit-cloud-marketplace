package org.sit.cloud.marketplace.actors;

import java.util.HashMap;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.QoS;
import org.sit.cloud.marketplace.utils.GaussianDistribution;
import org.sit.cloud.marketplace.utils.TimeKeeper;

public class GaussianProvider extends Provider {
	
	protected GaussianDistribution gaussianDistribution;
	
	public GaussianProvider(String id, int cores, int ram, int storage, double cost, double promisedAvailability, double promisedBandwidth, int vmCapacity, boolean isBadProvider){
		super(id, cores, ram, storage, cost, promisedAvailability, promisedBandwidth, vmCapacity, isBadProvider);
		gaussianDistribution = new GaussianDistribution(100, 20, id);
	}
	
	public Map<String, QoS> getQosExperiencedByVms(){
		Map<String, QoS> map = new HashMap<String, QoS>();
		// IF ACTUAL QoS > PROMISED QoS, THEN RETURN PROMISED VALUE
		for(String vmId : runningVmIdToVmMap.keySet()){
			//if(isBadProvider){
				//if(TimeKeeper.getTime() >= TimeKeeper.VIOLATION_START)// && TimeKeeper.getTime() <= TimeKeeper.VIOLATION_END)
				//	map.put(vmId, new QoS(0.01*gaussianDistribution.nextGaussianValueForQoSDegradation()*getPromisedAvailability(), 0.01*gaussianDistribution.nextGaussianValueForQoSDegradation()*getPromisedBandwidth()));
				//else
					//map.put(vmId, new QoS(getPromisedAvailability(), getPromisedBandwidth()));
			//}else{
				map.put(vmId, new QoS(getPromisedAvailability(), getPromisedBandwidth()));
			//}
		}
		return map;
	}
	
	public ProviderParams getExperiencedProviderParams(){
		if(isBadProvider)
			return new ProviderParams(id, 0.01*gaussianDistribution.nextGaussianValueForQoSDegradation()*getPromisedAvailability(), cost, 0.01*gaussianDistribution.nextGaussianValueForQoSDegradation()*getPromisedBandwidth(), cores, ram, storage);
		return getPromisedQos();
	}
	

}
