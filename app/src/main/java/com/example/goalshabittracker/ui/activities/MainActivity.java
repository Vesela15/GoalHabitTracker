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
import androidx.fragment.app.FragmentTransaction;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.ui.fragments.AchievementsFragment;
import com.example.goalshabittracker.ui.fragments.HabitsListFragment;
import com.example.goalshabittracker.ui.fragments.SettingsFragment;
import com.example.goalshabittracker.utils.LanguageUtils;
import com.example.goalshabittracker.utils.PreferencesManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (itemId == R.id.navigation_home) {
                setSupportActionBarTitle(R.string.goals_list);
                transaction.replace(R.id.fragment_container, new HabitsListFragment());
            } else if (itemId == R.id.navigation_achievements) {
                setSupportActionBarTitle(R.string.completed_goals);
                transaction.replace(R.id.fragment_container, new AchievementsFragment());
            } else if (itemId == R.id.navigation_settings) {
                setSupportActionBarTitle(R.string.settings);
                transaction.replace(R.id.fragment_container, new SettingsFragment());
            } else {
                return false;
            }

            transaction.commitAllowingStateLoss();
            return true;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.navigation_home);
        }

        setupNotificationPermission();

        new Thread(this::logFCMToken).start();
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

    private void setSupportActionBarTitle(int title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}