package com.example.goalshabittracker.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.models.Habit;


public class AddEditHabitDialogFragment extends DialogFragment {
    private EditText etName, etDescription, etCategory;
    private NumberPicker npFrequency;
    private Button btnSave, btnCancel;

    private Habit habitToEdit;
    private OnHabitSavedListener listener;

    public interface OnHabitSavedListener {
        void onHabitSaved(Habit habit);
    }

    public static AddEditHabitDialogFragment newInstance(Habit habit) {
        AddEditHabitDialogFragment fragment = new AddEditHabitDialogFragment();
        Bundle args = new Bundle();
        if (habit != null) {
            args.putString("habitId", habit.getId());
            args.putString("habitName", habit.getName());
            args.putString("habitDescription", habit.getDescription());
            args.putString("habitCategory", habit.getCategory());
            args.putInt("habitFrequency", habit.getTargetFrequency());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_habit, null);

        initViews(view);
        setupViews();
        loadHabitData();

        builder.setView(view)
                .setTitle(habitToEdit == null ? getString(R.string.add_habit) : getString(R.string.edit_habit));

        return builder.create();
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etHabitName);
        etDescription = view.findViewById(R.id.etHabitDescription);
        etCategory = view.findViewById(R.id.etHabitCategory);
        npFrequency = view.findViewById(R.id.npFrequency);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    private void setupViews() {
        npFrequency.setMinValue(1);
        npFrequency.setMaxValue(7);
        npFrequency.setValue(3);

        btnSave.setOnClickListener(v -> saveHabit());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void loadHabitData() {
        Bundle args = getArguments();
        if (args != null && args.containsKey("habitId")) {
            habitToEdit = new Habit();
            habitToEdit.setId(args.getString("habitId"));
            habitToEdit.setName(args.getString("habitName"));
            habitToEdit.setDescription(args.getString("habitDescription"));
            habitToEdit.setCategory(args.getString("habitCategory"));
            habitToEdit.setTargetFrequency(args.getInt("habitFrequency"));

            etName.setText(habitToEdit.getName());
            etDescription.setText(habitToEdit.getDescription());
            etCategory.setText(habitToEdit.getCategory());
            npFrequency.setValue(habitToEdit.getTargetFrequency());
        }
    }

    private void saveHabit() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        int frequency = npFrequency.getValue();

        if (name.isEmpty()) {
            etName.setError(getString(R.string.name_is_required));
            return;
        }

        if (category.isEmpty()) {
            etCategory.setError(getString(R.string.category_is_required));
            return;
        }

        Habit habit;
        if (habitToEdit == null) {
            habit = new Habit(name, description, category, frequency);
            habit.setActive(true);
            habit.setCreatedAt(System.currentTimeMillis());
        } else {
            habit = habitToEdit;
            habit.setName(name);
            habit.setDescription(description);
            habit.setCategory(category);
            habit.setTargetFrequency(frequency);
        }

        if (listener != null) {
            listener.onHabitSaved(habit);
        }

        dismiss();
    }

    public void setOnHabitSavedListener(OnHabitSavedListener listener) {
        this.listener = listener;
    }
}