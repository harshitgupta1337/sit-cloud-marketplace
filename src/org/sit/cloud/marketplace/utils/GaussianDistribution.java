package org.sit.cloud.marketplace.utils;


public class GaussianDistribution {
	
	private double mean;
	private double sd;
	
	public GaussianDistribution(double mean, double sd){
		this.mean = mean;
		this.sd = sd;
	}
	
	public double getValue(double x){
		double exp = -0.5 * ((x - mean)/sd) * ((x - mean)/sd);
		return (Math.exp(exp)/(Math.sqrt(2 * Math.PI * sd)));
	}
}
