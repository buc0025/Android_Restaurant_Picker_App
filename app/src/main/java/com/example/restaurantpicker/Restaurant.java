package com.example.restaurantpicker;

public class Restaurant {

    private String restaurantName;
    private String address;
    private String phoneNumber;

    public Restaurant(String restaurantName, String address, String phoneNumber) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
