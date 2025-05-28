package com.example.goalshabittracker.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "completed_habits")
public class CompletedHabit {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String habitId;
    private String habitName;
    private long completedAt;
    private String userId;

    public CompletedHabit(String habitId, String habitName, String userId) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.userId = userId;
        this.completedAt = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}