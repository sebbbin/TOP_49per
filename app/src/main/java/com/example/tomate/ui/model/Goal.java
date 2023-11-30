package com.example.tomate.ui.model;

import java.time.LocalDate;
import java.util.List;

public class Goal {
    private String userId;
    private String date;
    private int goal_tomato;
    public Goal() {
    }

    public Goal(String userId, LocalDate now, int goal_tomato) {
        this.userId = userId;
        this.date = now.toString();
        this.goal_tomato = goal_tomato;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGoal_tomato() {
        return goal_tomato;
    }

    public void setGoal_tomato(int goal_tomato) {
        this.goal_tomato = goal_tomato;
    }

}
