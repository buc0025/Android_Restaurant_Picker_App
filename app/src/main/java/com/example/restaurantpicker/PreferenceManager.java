package com.example.restaurantpicker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PreferenceManager {

    final private static String USER_PREFS = "USER_PREFS";
    final private static String USER_RESTAURANTS = "USER_RESTAURANTS";

    private SharedPreferences sharedPreferences;
    private SharedPreferences restaurantPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        restaurantPreferences = context.getSharedPreferences(USER_RESTAURANTS, Context.MODE_PRIVATE);
    }

    public void savePrefs(UserPreferences userPreferences, String uId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userPreferences);
        editor.putString(uId, json);
        editor.apply();
    }

    public ArrayList<String> getUserPrefs(String uId) {
        ArrayList<String> cuisines = new ArrayList<>();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(uId, null);
        UserPreferences obj = gson.fromJson(json, UserPreferences.class);

        cuisines = obj.getCuisines();
        return cuisines;
    }

    public void saveRestaurants(ArrayList<Restaurant> restaurants, String uId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurants);
        editor.putString(uId, json);
        editor.apply();
    }

    // Shared preference will have one restaurant as value and its name as the key
    public void saveRestaurant(String name, Restaurant restaurant) {
        SharedPreferences.Editor editor = restaurantPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        editor.putString(name, json);
        editor.apply();
    }

    //Need to add restaurants to UserRestaurants before using this method
//    public ArrayList<Restaurant> getRestaurant(String uId) {
//        ArrayList<Restaurant> restaurants = new ArrayList<>();
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(uId, null);
//        Restaurant obj = gson.fromJson(json, Restaurant.class);
//
//        for (Restaurant restaurant : obj)
//
//
//    }

    public void removeRestaurants(String uId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(uId);
        editor.apply();
    }

    public void removePrefs(String uId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(uId);
        editor.apply();
    }
}
