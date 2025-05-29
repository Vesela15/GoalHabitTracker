package com.example.goalshabittracker.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.ui.fragments.AchievementsFragment;
import com.example.goalshabittracker.ui.fragments.HabitsListFragment;
import com.example.goalshabittracker.ui.fragments.SettingsFragment;
import com.example.goalshabittracker.utils.LanguageUtils;
import com.example.goalshabittracker.utils.PreferencesManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferencesManager preferenceManager = new PreferencesManager(this);
        LanguageUtils.setLocale(this, preferenceManager.getLanguage());

        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HabitsListFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.navigation_achievements) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AchievementsFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.navigation_settings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.navigation_home);

        setupNotificationPermission();
        logFCMToken();
        setupAnalytics();
    }

    private void setupNotificationPermission() {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                }
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void logFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                    }
                });
    }

    private void setupAnalytics() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString("screen_name", "main");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params);
    }
}