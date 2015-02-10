package org.sit.cloud.marketplace.utils;

import migrdecidernew.migrdecidernew;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class MathsChecker {

	static double sigma = 0.15;
	static double alpha = 0.8;
	
	public static void checkExpectedFCalculation(){
		for(int i=0;i<100;i++){
			System.out.println(1 - sigma*Math.sqrt(2/Math.PI)*(1-Math.pow(alpha, i)));
		}
	}
	
	public static double calculateExpectedFValueForTime(int t){
		return(1 - sigma*Math.sqrt(2/Math.PI)*(1-Math.pow(alpha, t)));
	}
	
	public static void checkTimeBeforeDSSGoesBelowThreshold() throws MWException{
		double satisfactionValues[][] = new double[10][2];
		migrdecidernew theMigration = new migrdecidernew();
		for(int t=0;t<10;t++){
			satisfactionValues[t][0] = calculateExpectedFValueForTime(t)*1.6;
			satisfactionValues[t][1] = calculateExpectedFValueForTime(t)*1.6;
		}
		//satisfactionValues[9][0] = 0.921219864133949*1.6;
		//satisfactionValues[9][1] = 0.9217083088377886*1.6;
		Object[] y = null;
		MWNumericArray x = new MWNumericArray(satisfactionValues, MWClassID.DOUBLE);
		//System.out.println(x.toArray().length);
		//y=theMigration.migration(1,x);
		y=theMigration.migration_shrink(1,x);
		
		String[] parts = y[0].toString().split("\n");
		for(int t=0;t<10;t++){
			System.out.println(Double.parseDouble(parts[t]));
		}	
	}
	
	public static void main(String args[]) throws MWException{
		checkTimeBeforeDSSGoesBelowThreshold();
	}
}
