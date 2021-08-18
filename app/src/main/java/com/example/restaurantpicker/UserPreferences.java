package com.example.restaurantpicker;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {

    private String zipCode;
    private int radius;
    private ArrayList<String> cuisines;
    private boolean openNow;

    public UserPreferences(String zipCode, int radius, ArrayList<String> cuisines, boolean openNow) {
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(ArrayList<String> cuisines) {
        this.cuisines = cuisines;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
