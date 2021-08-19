package com.example.restaurantpicker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PreferenceManager {

    final private static String USER_PREFS = "USER_PREFS";
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
    }

    public void savePrefs(UserPreferences userPreferences, String uID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userPreferences);
        editor.putString(uID, json);
        editor.apply();
    }

    public String getUserPrefs(String uId) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(uId, null);
        UserPreferences obj = gson.fromJson(json, UserPreferences.class);
        String zipcode = obj.getZipCode();
        return zipcode;
    }

    public void removePrefs(String uId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(uId);
        editor.apply();
    }
}
