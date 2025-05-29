package com.example.goalshabittracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.adapters.CompletedHabitsAdapter;
import com.example.goalshabittracker.room.AppDatabase;
import com.example.goalshabittracker.room.CompletedHabit;
import com.example.goalshabittracker.room.CompletedHabitDao;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CompletedHabitsAdapter adapter;
    private CompletedHabitDao completedHabitDao;
    private final List<CompletedHabit> completedHabits = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAchievements);
        mAuth = FirebaseAuth.getInstance();
        completedHabitDao = AppDatabase.getInstance(requireContext()).completedHabitDao();

        setupRecyclerView();
        loadCompletedHabits();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new CompletedHabitsAdapter(completedHabits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCompletedHabits() {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        completedHabitDao.getAllCompletedHabits(userId).observe(getViewLifecycleOwner(), habits -> {
            completedHabits.clear();
            completedHabits.addAll(habits);
            adapter.notifyDataSetChanged();
        });
    }
}