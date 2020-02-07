package com.example.indhan;

public class PumpContentClass {

    String pumpName;
    String googleRating, indhanRating, sanity, restaurantRating, behaviour;
    double latitude, longitude;
    boolean washroom, restaurant,  cashless, air;


    public PumpContentClass(String pumpName, String googleRating, String behaviour, double latitude, double longitude,
                            String sanity, String restaurantRating, boolean washroom, boolean restaurant, boolean cashless) {
        this.pumpName = pumpName;
        this.googleRating = googleRating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sanity = sanity;
        this.washroom = washroom;
        this.restaurant = restaurant;
        this.cashless = cashless;
        this.restaurantRating = restaurantRating;
        this.behaviour = behaviour;
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

    public String getSanity() {
        return sanity;
    }

    public String getrestaurantRating() {
        return restaurantRating;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public boolean isWashroom() {
        return washroom;
    }

    public boolean isRestaurant() {
        return restaurant;
    }

    public boolean isCashless() {
        return cashless;
    }

    public boolean isAir() {
        return air;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
