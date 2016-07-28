package com.android.mapbox;

/**
 * Created by Hanani on 28/7/2016.
 */
public class User {

    public String plate, profile;
    public Double lat, lng;

    public User (String plate, String profile, Double lat, Double lng){
        this.plate = plate;
        this.profile = profile;
        this.lat = lat;
        this.lng = lng;
    }

    public String getPlate() {
        return plate;
    }

    public String getProfile() {
        return profile;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
