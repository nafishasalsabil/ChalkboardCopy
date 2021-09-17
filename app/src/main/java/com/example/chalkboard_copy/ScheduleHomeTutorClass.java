package com.example.chalkboard_copy;

public class ScheduleHomeTutorClass {
    String time,course_name,batch,date,day;

    public ScheduleHomeTutorClass() {
    }

    public ScheduleHomeTutorClass(String time, String course_name, String batch, String date, String day) {
        this.time = time;
        this.course_name = course_name;
        this.batch = batch;
        this.date = date;
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

 public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    @Override
    public String toString() {
        return day;
    }
}
