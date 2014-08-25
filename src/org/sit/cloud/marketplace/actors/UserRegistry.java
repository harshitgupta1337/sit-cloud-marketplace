package org.sit.cloud.marketplace.actors;

import java.util.HashMap;
import java.util.Map;

public class UserRegistry {
private Map<String, User> userIdtoUserMap;
	
	public UserRegistry(){
		setUserIdtoUserMap(new HashMap<String, User>());
	}

	public void registerUser(User user){
		userIdtoUserMap.put(user.getId(), user);
	}

	public Map<String, User> getUserIdtoUserMap() {
		return userIdtoUserMap;
	}

	public void setUserIdtoUserMap(Map<String, User> userIdtoUserMap) {
		this.userIdtoUserMap = userIdtoUserMap;
	}
}
