package org.sit.cloud.marketplace.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
	
	private double generateStandardNormal(){
		double rand1 = random.nextDouble();
		double rand2 = random.nextDouble();
		double theta = 2*Math.PI*rand2;
		double rho = Math.sqrt(-2*Math.log(rand1));
		double z0 = rho*Math.cos(theta);
		double z1 = rho*Math.sin(theta);
		
		return Math.abs(z0);
	}
	
	public double nextGaussianValueForQoSDegradation(){
		// Uses Box-Muller transformation to convert a uniformly distributed random variable to Gaussian
		/*double theta = 2*Math.PI*random.nextDouble();
		double rho = Math.sqrt(-2*Math.log(1-random.nextDouble()));
		double scale = sd * rho;
		double result = mean + scale*Math.cos(theta);
		if(result > 100)
			return nextGaussianValueForQoSDegradation();
		return result;*/
		double changedSd = generateStandardNormal() * sd;
		return 1-changedSd;
	}
	
	
	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("InputData/11.txt", "UTF-8");
		GaussianDistribution dist = new GaussianDistribution(0, 0.25, "11");
		double sum = 0;
		System.out.println(1-0.25*Math.sqrt(2/Math.PI));
		for(int i=0;i<10000;i++){
			
			sum += dist.nextGaussianValueForQoSDegradation();
			
			//System.out.println(i);
		}
		System.out.println(sum/10000);
		writer.close();
	}
}
