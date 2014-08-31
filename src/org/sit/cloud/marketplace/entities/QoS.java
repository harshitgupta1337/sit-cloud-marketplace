package org.sit.cloud.marketplace.entities;

public class QoS {

	private double availability;
	private double bandwidth;
	
	public QoS(double availability, double bandwidth){
		this.setAvailability(availability);
		this.setBandwidth(bandwidth);
	}

	public double getAvailability() {
		return availability;
	}

	public void setAvailability(double availability) {
		this.availability = availability;
	}

	public double getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}
}
