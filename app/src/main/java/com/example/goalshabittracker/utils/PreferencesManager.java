package com.example.goalshabittracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREF_NAME = "AppPreferences";
    private static final String KEY_LANGUAGE = "language";

    private final SharedPreferences preferences;

    public PreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLanguage(String languageCode) {
        preferences.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }

    public String getLanguage() {
        return preferences.getString(KEY_LANGUAGE, "en");
    }
}