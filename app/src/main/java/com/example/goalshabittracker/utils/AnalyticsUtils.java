package com.example.goalshabittracker.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUtils {
    private final FirebaseAnalytics firebaseAnalytics;

    public AnalyticsUtils(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void logHabitsLoaded(int count) {
        Bundle params = new Bundle();
        params.putInt("habits_count", count);
        firebaseAnalytics.logEvent("habits_loaded", params);
    }

    public void logHabitAdded(String habitName) {
        Bundle params = new Bundle();
        params.putString("habit_name", habitName);
        firebaseAnalytics.logEvent("habit_added", params);
    }

    public void logHabitEdited(String habitName) {
        Bundle params = new Bundle();
        params.putString("habit_name", habitName);
        firebaseAnalytics.logEvent("habit_edited", params);
    }

    public void logHabitDeleted(String habitName) {
        Bundle params = new Bundle();
        params.putString("habit_name", habitName);
        firebaseAnalytics.logEvent("habit_deleted", params);
    }

    public void logAchievementsViewed(int count) {
        Bundle params = new Bundle();
        params.putInt("achievements_count", count);
        firebaseAnalytics.logEvent("achievements_viewed", params);
    }
}