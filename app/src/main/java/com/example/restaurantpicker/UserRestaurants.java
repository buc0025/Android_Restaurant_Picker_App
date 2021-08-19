package com.example.restaurantpicker;

import java.util.ArrayList;

public class UserRestaurants {

    private ArrayList<Restaurant> restaurants;

    public UserRestaurants() {

    }

    public UserRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void addRestaurants(Restaurant restaurant) {
        restaurants.add(restaurant);
    }
}
