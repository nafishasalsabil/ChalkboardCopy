package com.example.chalkboard_copy;

public class TodayClass {
    String time,description;

    public TodayClass() {
    }

    public TodayClass(String time, String description) {
        this.time = time;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
