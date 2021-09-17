package com.example.chalkboard_copy;

public class StudentItems {
    int id;
    String name;
    String status;
    String lecture_name;
    String lecture_date;
    String quiz_marks;

    boolean attendance;
    int checkedId ;

    public StudentItems() {
    }

    public StudentItems(int id, String name, String status, String lecture_name, String lecture_date, String quiz_marks, boolean attendance, int checkedId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.lecture_name = lecture_name;
        this.lecture_date = lecture_date;
        this.quiz_marks = quiz_marks;
        this.attendance = attendance;
        this.checkedId = checkedId;
    }

    public StudentItems(int id, String name, String status, String lecture_name, String lecture_date) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.lecture_name = lecture_name;
        this.lecture_date = lecture_date;
    }

    public int getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLecture_name() {
        return lecture_name;
    }

    public void setLecture_name(String lecture_name) {
        this.lecture_name = lecture_name;
    }

    public String getLecture_date() {
        return lecture_date;
    }

    public void setLecture_date(String lecture_date) {
        this.lecture_date = lecture_date;
    }

    public String getQuiz_marks() {
        return quiz_marks;
    }

    public void setQuiz_marks(String quiz_marks) {
        this.quiz_marks = quiz_marks;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
