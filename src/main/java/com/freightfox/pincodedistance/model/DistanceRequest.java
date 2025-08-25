package com.freightfox.pincodedistance.model;

public class DistanceRequest {

    private String fromPincode;
    private String toPincode;
    private String country; // optional field

    public DistanceRequest() {}

    public DistanceRequest(String fromPincode, String toPincode, String country) {
        this.fromPincode = fromPincode;
        this.toPincode = toPincode;
        this.country = country;
    }

    public String getFromPincode() {
        return fromPincode;
    }

    public void setFromPincode(String fromPincode) {
        this.fromPincode = fromPincode;
    }

    public String getToPincode() {
        return toPincode;
    }

    public void setToPincode(String toPincode) {
        this.toPincode = toPincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
