package com.example.goalshabittracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.room.CompletedHabit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CompletedHabitsAdapter extends RecyclerView.Adapter<CompletedHabitsAdapter.ViewHolder> {
    private List<CompletedHabit> completedHabits;
    private SimpleDateFormat dateFormat;

    public CompletedHabitsAdapter(List<CompletedHabit> completedHabits) {
        this.completedHabits = completedHabits;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_completed_habit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompletedHabit habit = completedHabits.get(position);
        holder.bind(habit);
    }

    @Override
    public int getItemCount() {
        return completedHabits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewHabitName;
        private TextView textViewCompletedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHabitName = itemView.findViewById(R.id.textViewHabitName);
            textViewCompletedDate = itemView.findViewById(R.id.textViewCompletedDate);
        }

        public void bind(CompletedHabit habit) {
            textViewHabitName.setText(habit.getHabitName());
            String completedDate = dateFormat.format(new Date(habit.getCompletedAt()));
            textViewCompletedDate.setText(itemView.getContext().getString(R.string.completed) + completedDate);
        }
    }
}