package org.sit.cloud.marketplace.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sit.cloud.marketplace.actors.AlphaBroker;
import org.sit.cloud.marketplace.actors.Provider;
import org.sit.cloud.marketplace.actors.User;
import org.sit.cloud.marketplace.actors.ZeroToOneBroker;
import org.sit.cloud.marketplace.entities.UserRequest;

import com.mathworks.toolbox.javabuilder.MWException;

public class Runner {

	private static ZeroToOneBroker broker = new ZeroToOneBroker();
	
	private static int index = 0;
	private static int requestIndex = 0;
	private static int NO_OF_REQUESTS = 100;
	
	private static List<Long> userRequestInstants = getUserRequestInstants();
	private static List<UserRequest> userRequests;
	
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
		for(int i=0;i<NO_OF_REQUESTS;i++){
			userRequestInstants.add((long)(Math.random() * TimeKeeper.END));
		}
		return sort(userRequestInstants);
	}
	
	private static void fillUserRequests() throws IOException{
		userRequests = GetUserRequestsFromInputData.getUserRequestFromInputData();
	}
	
	private static void generateUserRequest(){
		if(index < userRequestInstants.size() && userRequestInstants.get(index) == TimeKeeper.getTime()){
			index++;
			
			if(requestIndex < userRequests.size()){
				User user = new User();
				UserRequest userRequest = userRequests.get(requestIndex++);
				
				user.setId(userRequest.getUserId());
				broker.registerUser(user);
				broker.acceptUserRequest(userRequest);
			}
				
			//UserRequest request = new UserRequest(user.getId(), 1, 70, 62, 2, 1, 250, 200, (Math.random()<0.4)?true:false);
		}
		else if(index < userRequestInstants.size() && userRequestInstants.get(index) <= TimeKeeper.getTime()){
			index++;
		}
	}
	
	public static List<Provider> constructProviders(boolean gaussianWanted) throws IOException{
		return GetProviderFromInputData.getProviderFromInputData(gaussianWanted);
	}
	
	public static void main(String args[]) throws IOException, MWException{
		fillUserRequests();
		for(Provider provider : constructProviders(true)){
			broker.registerProvider(provider);
		}
		User user = new User();
		broker.registerUser(user);
		while(TimeKeeper.tick()){
			generateUserRequest();
			broker.performMonitoringAndMigrations();
		}
		//broker.printAverageCost();
		broker.printSimulationDetailsWrtUserReq();;
		System.out.println("DONE !!!!");
	}
	
	
	
}
