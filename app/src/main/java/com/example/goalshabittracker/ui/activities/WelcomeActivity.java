package com.example.goalshabittracker.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.utils.LanguageUtils;
import com.example.goalshabittracker.utils.PreferencesManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferencesManager preferenceManager = new PreferencesManager(this);
        LanguageUtils.setLocale(this, preferenceManager.getLanguage());

        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity();
            return;
        }

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        ImageButton languageButton = findViewById(R.id.languageButton);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        languageButton.setOnClickListener(v -> showLanguageDialog());
    }

    private void showLanguageDialog() {
        String[] languages = {getString(R.string.english), getString(R.string.macedonian), getString(R.string.german)};
        String[] languageCodes = {"en", "mk", "de"};

        new AlertDialog.Builder(this)
                .setTitle(R.string.select_language)
                .setItems(languages, (dialog, which) -> {
                    // Save language preference
                    PreferencesManager preferencesManager = new PreferencesManager(this);
                    preferencesManager.setLanguage(languageCodes[which]);

                    // Change language
                    LanguageUtils.setLocale(this, languageCodes[which]);

                    // Restart the app to apply changes
                    recreate();
                })
                .show();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
