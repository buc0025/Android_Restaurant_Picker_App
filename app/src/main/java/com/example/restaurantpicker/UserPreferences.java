package com.example.restaurantpicker;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {

    private String zipCode;
    private String radius;
    private ArrayList<String> cuisines;
    private String openNow;

    public UserPreferences(String zipCode, String radius, ArrayList<String> cuisines, String openNow) {
        this.zipCode = zipCode;
        this.radius = radius;
        this.cuisines = cuisines;
        this.openNow = openNow;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public ArrayList<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(ArrayList<String> cuisines) {
        this.cuisines = cuisines;
    }

    public String isOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }
}
