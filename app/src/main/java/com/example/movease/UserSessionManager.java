package com.example.movease;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String PREF_NAME = "MoveasePref";
    private static final String KEY_ROLE = "userRole";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public UserSessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUserRole(String role) {
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public String getUserRole() {
        return pref.getString(KEY_ROLE, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}