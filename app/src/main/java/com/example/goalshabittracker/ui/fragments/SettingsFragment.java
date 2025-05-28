package com.example.goalshabittracker.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.ui.activities.WelcomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private TextView tvUserEmail, tvUserName;
    private MaterialButton btnLanguage, btnLogout;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign In client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        displayUserInfo();
        setClickListeners();
    }

    private void initViews(View view) {
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserName = view.findViewById(R.id.tvUserName);
        btnLanguage = view.findViewById(R.id.btnLanguage);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void displayUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvUserEmail.setText(user.getEmail());
            tvUserName.setText(user.getDisplayName());
        }
    }

    private void setClickListeners() {
        btnLanguage.setOnClickListener(v -> showLanguageDialog());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Macedonian"};
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.select_language)
                .setItems(languages, (dialog, which) -> {
                    // Handle language change
                    // You'll need to implement the actual language change logic
                })
                .show();
    }

    private void logout() {
        // Sign out from Google
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            // Sign out from Firebase
            mAuth.signOut();

            Intent intent = new Intent(requireContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}