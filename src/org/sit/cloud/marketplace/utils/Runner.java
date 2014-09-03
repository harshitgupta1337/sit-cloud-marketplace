package org.sit.cloud.marketplace.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sit.cloud.marketplace.actors.Broker;
import org.sit.cloud.marketplace.actors.User;
import org.sit.cloud.marketplace.entities.GeoLocation;
import org.sit.cloud.marketplace.entities.UserRequest;

public class Runner {

	private static Broker broker = new Broker();
	
	private static int index = 0;
	private static List<Long> userRequestInstants = getUserRequestInstants();
	
	private static List<Long> sort(List<Long> input){
		for(int i=0;i<input.size();i++){
			int small = i;
			for(int j=i;j<input.size();j++){
				if(input.get(j) < input.get(small))
					small = j;
			}
			long temp = input.get(i);
			input.set(i, input.get(small));
			input.set(small, temp);
		}
		return input;
	}
	
	private static List<Long> getUserRequestInstants(){
		List<Long> userRequestInstants = new ArrayList<Long>();
		for(int i=0;i<1000;i++){
			userRequestInstants.add((long)(Math.random() * TimeKeeper.END));
		}
		return sort(userRequestInstants);
	}
	
	private static void generateUserRequest(){
		if(index < userRequestInstants.size() && userRequestInstants.get(index) == TimeKeeper.getTime()){
			index++;
			User user = new User();
			broker.registerUser(user);
			Map<GeoLocation, Integer> map = new HashMap<GeoLocation, Integer>() {{ put(GeoLocation.EU_WEST, 10); }};
			UserRequest request = new UserRequest(user.getId(), map, 0.0, 0.0, 0, 0, 0, 0.0, false);
			broker.acceptUserRequest(request);
		}
		else if(index < userRequestInstants.size() && userRequestInstants.get(index) <= TimeKeeper.getTime()){
			index++;
		}
	}
	public static void main(String args[]) throws IOException{
		
		System.out.println(userRequestInstants);
		
		while(TimeKeeper.tick()){
			generateUserRequest();
			broker.monitorVms();
		}
	}
	
	
	
}
