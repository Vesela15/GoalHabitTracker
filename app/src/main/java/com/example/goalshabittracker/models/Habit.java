package com.example.goalshabittracker.models;

public class Habit {
    private String id;
    private String name;
    private String description;
    private String category;
    private int targetFrequency; // per week
    private long createdAt;
    private boolean isActive;
    private String userId;

    public Habit() {
    }

    public Habit(String name, String description, String category, int targetFrequency) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.targetFrequency = targetFrequency;
        this.createdAt = System.currentTimeMillis();
        this.isActive = true;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTargetFrequency() {
        return targetFrequency;
    }

    public void setTargetFrequency(int targetFrequency) {
        this.targetFrequency = targetFrequency;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}