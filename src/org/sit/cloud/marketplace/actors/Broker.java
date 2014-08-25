package org.sit.cloud.marketplace.actors;

public class Broker {
	private Registry registry;
	private UserRegistry userRegistry;
	public Broker(){
		registry = new Registry();
		userRegistry = new UserRegistry();
	}

	public void registerProvider(Provider provider){
		registry.registerProvider(provider);
	}

	public void registerUser(User user){
		userRegistry.registerUser(user);
	}
	
	
	
	
	
	
	/**
	 * @return the registry
	 */
	public Registry getRegistry() {
		return registry;
	}

	/**
	 * @param registry the registry to set
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public UserRegistry getUserRegistry() {
		return userRegistry;
	}

	public void setUserRegistry(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}
}
