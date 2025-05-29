package com.example.goalshabittracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalshabittracker.R;
import com.example.goalshabittracker.models.Habit;

import java.util.List;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitViewHolder> {
    private List<Habit> habits;
    private OnHabitListener listener;

    public interface OnHabitListener {
        void onHabitClick(Habit habit);

        void onDeleteClick(Habit habit);

        void onHabitCompleted(Habit habit, boolean isCompleted);
    }

    public HabitsAdapter(List<Habit> habits, OnHabitListener listener) {
        this.habits = habits;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.bind(habit);
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    class HabitViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvCategory, tvFrequency;
        private ImageButton btnDelete;
        private CardView cardView;
        private CheckBox checkBoxComplete;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvHabitName);
            tvDescription = itemView.findViewById(R.id.tvHabitDescription);
            tvCategory = itemView.findViewById(R.id.tvHabitCategory);
            tvFrequency = itemView.findViewById(R.id.tvHabitFrequency);
            btnDelete = itemView.findViewById(R.id.btnDeleteHabit);
            cardView = itemView.findViewById(R.id.cardViewHabit);
            checkBoxComplete = itemView.findViewById(R.id.checkBoxComplete);
        }

        public void bind(Habit habit) {
            tvName.setText(habit.getName());
            tvDescription.setText(habit.getDescription());
            tvCategory.setText(habit.getCategory());
            tvFrequency.setText(habit.getTargetFrequency() + itemView.getContext().getString(R.string.times_week));

            cardView.setOnClickListener(v -> listener.onHabitClick(habit));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(habit));

            checkBoxComplete.setOnCheckedChangeListener(null);
            checkBoxComplete.setChecked(false); // Default unchecked
            checkBoxComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listener.onHabitCompleted(habit, isChecked);
            });
        }
    }
}