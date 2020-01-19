package com.example.indhan;

public class PumpContentClass {

    String pumpName;
    String googleRating, indhanRating;
    double latitude, longitude;


    public PumpContentClass(String pumpName, String googleRating, String indhanRating, double latitude, double longitude) {
        this.pumpName = pumpName;
        this.googleRating = googleRating;
        this.indhanRating = indhanRating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPumpName() {
        return pumpName;
    }

    public String getGoogleRating() {
        return googleRating;
    }

    public String getIndhanRating() {
        return indhanRating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
