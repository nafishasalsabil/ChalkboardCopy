package com.example.chalkboard_copy;

public class ScheduleClass {
    String time,course_name,section,room,date,day;

    public ScheduleClass() {
    }

    public ScheduleClass(String time, String course_name, String section, String room, String date, String day) {
        this.time = time;
        this.course_name = course_name;
        this.section = section;
        this.room = room;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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
