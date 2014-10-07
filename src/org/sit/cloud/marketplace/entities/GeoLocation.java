package org.sit.cloud.marketplace.entities;

public enum GeoLocation{
    US_WEST(1), US_EAST(2), EU_WEST(3), EU_EAST(4), SE_ASIA(5), AU(6);
    
    private int code;

    private GeoLocation(int code) {
            this.setCode(code);
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
};   