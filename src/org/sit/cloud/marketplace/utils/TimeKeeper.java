package org.sit.cloud.marketplace.utils;

public class TimeKeeper {

	private static long SIMULATION_TIME = 0;
	private static long END = 86400;
	
	
	public static boolean tick(){
		if(SIMULATION_TIME == END)
			return false;
		SIMULATION_TIME++;
		return true;
	}
	
	public static long getTime(){
		return SIMULATION_TIME;
	}
	
}