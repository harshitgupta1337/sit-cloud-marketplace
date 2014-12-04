package org.sit.cloud.marketplace.utils;

public class TimeKeeper {

	private static long SIMULATION_TIME = 0;
	//public static long END = 61320;
	public static long END = 10000;
	public static long VIOLATION_START = END/4;
	public static long VIOLATION_END = 3*END/4;
	
	public static void initialize(){
		SIMULATION_TIME = 0;
	}
	
	public static boolean shouldViolationsBeCalculated(){
		if(SIMULATION_TIME % (24*7) == 0){
			return true;
		}
		return false;
	}
	
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
