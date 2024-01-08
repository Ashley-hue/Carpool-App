package com.example.carpoolingapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SessionManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_OWNER_PHONE = "phoneNumber";

    public SessionManager(@NonNull Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
    }

    // Store user session data
    public void createLoginSession(String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    //Store cars registration number
    public void savePhone(String phone) {
        editor.putString(KEY_OWNER_PHONE, phone);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL, null);
    }

    public String getKeyOwnerPhone() {return preferences.getString(KEY_OWNER_PHONE, null);}

    // Clear the user session data
    public void logoutUser() {
        // Clear all data from shared preferences
        editor.clear();
        editor.commit();
    }

}
