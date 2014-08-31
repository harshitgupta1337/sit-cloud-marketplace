package org.sit.cloud.marketplace.entities;

public class SlaViolationData {

	private double promisedAvailability;
	private double experiencedAvailability;
	private double promisedBandwidth;
	private double experiencedBandwidth;
	private double violationOfAvailability;
	private double violationOfBandwidth;
	
	public SlaViolationData(double promisedAvailability, double experiencedAvailability, double promisedBandwidth, double experiencedBandwidth){
		this.promisedAvailability = promisedAvailability;
		this.promisedBandwidth = promisedBandwidth;
		this.experiencedAvailability = experiencedAvailability;
		this.experiencedBandwidth = experiencedBandwidth;
		calculateBandwidthViolation();
		calculateAvailabilityViolation();
	}
	
	private void calculateBandwidthViolation(){
		violationOfBandwidth = 0;
	}
	private void calculateAvailabilityViolation(){
		violationOfAvailability = 0;
	}
	public double getPromisedAvailability() {
		return promisedAvailability;
	}
	public void setPromisedAvailability(double promisedAvailability) {
		this.promisedAvailability = promisedAvailability;
	}
	public double getExperiencedAvailability() {
		return experiencedAvailability;
	}
	public void setExperiencedAvailability(double experiencedAvailability) {
		this.experiencedAvailability = experiencedAvailability;
	}
	public double getPromisedBandwidth() {
		return promisedBandwidth;
	}
	public void setPromisedBandwidth(double promisedBandwidth) {
		this.promisedBandwidth = promisedBandwidth;
	}
	public double getExperiencedBandwidth() {
		return experiencedBandwidth;
	}
	public void setExperiencedBandwidth(double experiencedBandwidth) {
		this.experiencedBandwidth = experiencedBandwidth;
	}
	public double getViolationOfAvailability() {
		return violationOfAvailability;
	}
	public void setViolationOfAvailability(double violationOfAvailability) {
		this.violationOfAvailability = violationOfAvailability;
	}
	public double getViolationOfBandwidth() {
		return violationOfBandwidth;
	}
	public void setViolationOfBandwidth(double violationOfBandwidth) {
		this.violationOfBandwidth = violationOfBandwidth;
	}
}
