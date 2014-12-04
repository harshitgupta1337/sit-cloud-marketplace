package org.sit.cloud.marketplace.utils;

import java.util.Random;


public class GaussianDistribution {
	
	private double mean;
	private double sd;
	private Random random;
	
	public GaussianDistribution(double mean, double sd, String seed){
		this.mean = mean;
		this.sd = sd;
		random = new Random(Long.parseLong(seed));
	}
	
	public double getValue(double x){
		double exp = -0.5 * ((x - mean)/sd) * ((x - mean)/sd);
		return (Math.exp(exp)/(sd * Math.sqrt(2 * Math.PI)));
	}
	
	public double nextGaussianValueForQoSDegradation(){
		double theta = 2*Math.PI*random.nextDouble();
		double rho = Math.sqrt(-2*Math.log(1-random.nextDouble()));
		double scale = sd + rho;
		double result = mean + scale*Math.cos(theta);
		if(result > 100)
			return nextGaussianValueForQoSDegradation();
		return result;
	}
	
	public static void main(String args[]){
		GaussianDistribution dist = new GaussianDistribution(100, 20, "21");
		for(int i=0;i<100;i++)
			System.out.println(dist.nextGaussianValueForQoSDegradation());
	}
}
