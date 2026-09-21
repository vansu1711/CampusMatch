package com.example.campusmatch;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "CampusMatchSession";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public void createLoginSession(String username, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}