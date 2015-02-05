package org.sit.cloud.marketplace.actors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.sit.cloud.marketplace.entities.ProviderParams;
import org.sit.cloud.marketplace.entities.QoS;
import org.sit.cloud.marketplace.utils.TimeKeeper;

public class FileBasedProvider extends Provider {
	
	BufferedReader br;
	
	public FileBasedProvider(String id, int cores, int ram, int storage, double cost, double promisedAvailability, double promisedBandwidth, int vmCapacity, boolean isBadProvider) throws FileNotFoundException, UnsupportedEncodingException{
		super(id, cores, ram, storage, cost, promisedAvailability, promisedBandwidth, vmCapacity, isBadProvider);
		
		br = new BufferedReader(new FileReader("InputData/"+id+".txt"));
	}
	
	private double getNextGaussian() throws IOException{
		String line = br.readLine();
		return Double.parseDouble(line);
	}
	
	public Map<String, QoS> getQosExperiencedByVms(){
		Map<String, QoS> map = new HashMap<String, QoS>();
		// IF ACTUAL QoS > PROMISED QoS, THEN RETURN PROMISED VALUE
		for(String vmId : runningVmIdToVmMap.keySet()){
			if(isBadProvider){
				if(TimeKeeper.getTime() >= TimeKeeper.VIOLATION_START)// && TimeKeeper.getTime() <= TimeKeeper.VIOLATION_END)
					try{
						map.put(vmId, new QoS(0.01*getNextGaussian()*getPromisedAvailability(), 0.01*getNextGaussian()*getPromisedBandwidth()));
					}catch(Exception e){
						
					}
				else
					map.put(vmId, new QoS(getPromisedAvailability(), getPromisedBandwidth()));
			}else{
				map.put(vmId, new QoS(getPromisedAvailability(), getPromisedBandwidth()));
			}
		}
		return map;
	}
	/*
	public ProviderParams getExperiencedProviderParams(){
		if(isBadProvider)
			return new ProviderParams(id, 0.01*gaussianDistribution.nextGaussianValueForQoSDegradation()*getPromisedAvailability(), cost, 0.01*gaussianDistribution.nextGaussianValueForQoSDegradation()*getPromisedBandwidth(), cores, ram, storage);
		return getPromisedQos();
	}
	*/

}
