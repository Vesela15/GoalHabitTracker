package com.example.goalshabittracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.adapters.HabitsAdapter;
import com.example.goalshabittracker.dialogs.AddEditHabitDialogFragment;
import com.example.goalshabittracker.models.Habit;
import com.example.goalshabittracker.room.AppDatabase;
import com.example.goalshabittracker.room.CompletedHabit;
import com.example.goalshabittracker.room.CompletedHabitDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HabitsListFragment extends Fragment {
    private RecyclerView recyclerView;
    private HabitsAdapter adapter;
    private FloatingActionButton fabAdd;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Habit> habitsList;

    private CompletedHabitDao completedHabitDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        completedHabitDao = AppDatabase.getInstance(requireContext()).completedHabitDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits_list, container, false);

        initViews(view);
        setupFirestore();
        setupRecyclerView();
        setupClickListeners();
        loadHabits();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewHabits);
        fabAdd = view.findViewById(R.id.fabAddHabit);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupFirestore() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        habitsList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new HabitsAdapter(habitsList, new HabitsAdapter.OnHabitListener() {
            @Override
            public void onHabitClick(Habit habit) {
                openEditHabitDialog(habit);
            }

            @Override
            public void onDeleteClick(Habit habit) {
                showDeleteConfirmationDialog(habit);
            }

            @Override
            public void onHabitCompleted(Habit habit, boolean isCompleted) {
                if (mAuth.getCurrentUser() == null) return;
                String userId = mAuth.getCurrentUser().getUid();

                // Update Firestore
                db.collection("habits")
                        .document(habit.getId())
                        .update("completed", isCompleted)
                        .addOnSuccessListener(aVoid -> {
                            // Store in Room if completed
                            if (isCompleted) {
                                CompletedHabit completedHabit = new CompletedHabit(
                                        habit.getId(),
                                        habit.getName(),
                                        userId
                                );
                                new Thread(() -> {
                                    completedHabitDao.insert(completedHabit);
                                }).start();
                            }
                            loadHabits();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error updating habit", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        fabAdd.setOnClickListener(v -> openAddHabitDialog());
    }

    private void loadHabits() {
        if (mAuth.getCurrentUser() == null) return;

        progressBar.setVisibility(View.VISIBLE);
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("habits")
                .whereEqualTo("userId", userId)
                .whereEqualTo("completed", false)  // Only get uncompleted habits
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    habitsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Habit habit = document.toObject(Habit.class);
                        habit.setId(document.getId());
                        habitsList.add(habit);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void openAddHabitDialog() {
        AddEditHabitDialogFragment dialog = AddEditHabitDialogFragment.newInstance(null);
        dialog.setOnHabitSavedListener(this::saveHabit);
        dialog.show(getParentFragmentManager(), "AddHabitDialog");
    }

    private void openEditHabitDialog(Habit habit) {
        AddEditHabitDialogFragment dialog = AddEditHabitDialogFragment.newInstance(habit);
        dialog.setOnHabitSavedListener(this::updateHabit);
        dialog.show(getParentFragmentManager(), "EditHabitDialog");
    }

    private void saveHabit(Habit habit) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();
        habit.setUserId(userId);

        db.collection("habits")
                .add(habit)
                .addOnSuccessListener(documentReference -> {
                    loadHabits();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding habit", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateHabit(Habit habit) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        // Verify the habit belongs to current user
        db.collection("habits").document(habit.getId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Habit existingHabit = documentSnapshot.toObject(Habit.class);
                    if (existingHabit != null && userId.equals(existingHabit.getUserId())) {
                        // Proceed with update
                        db.collection("habits").document(habit.getId())
                                .update("name", habit.getName(),
                                        "description", habit.getDescription(),
                                        "category", habit.getCategory(),
                                        "targetFrequency", habit.getTargetFrequency())
                                .addOnSuccessListener(aVoid -> {
                                    loadHabits();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Error updating habit", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    private void showDeleteConfirmationDialog(Habit habit) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Habit")
                .setMessage("Are you sure you want to delete \"" + habit.getName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> deleteHabit(habit))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteHabit(Habit habit) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        // Verify the habit belongs to current user
        db.collection("habits").document(habit.getId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Habit existingHabit = documentSnapshot.toObject(Habit.class);
                    if (existingHabit != null && userId.equals(existingHabit.getUserId())) {
                        // Proceed with delete
                        db.collection("habits").document(habit.getId())
                                .delete()  // This deletes the document completely
                                .addOnSuccessListener(aVoid -> {
                                    loadHabits();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Error deleting habit", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }
}