package com.example.goalshabittracker.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompletedHabitDao {
    @Insert
    long insert(CompletedHabit completedHabit);

    @Query("SELECT * FROM completed_habits WHERE userId = :userId ORDER BY completedAt DESC")
    LiveData<List<CompletedHabit>> getAllCompletedHabits(String userId);

    @Query("SELECT * FROM completed_habits WHERE habitId = :habitId AND " +
            "completedAt BETWEEN :startTime AND :endTime")
    List<CompletedHabit> getCompletedHabitsForPeriod(String habitId, long startTime, long endTime);
}