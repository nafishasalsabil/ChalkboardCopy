package com.example.chalkboard_copy;

public class QuizMarksClass {
    int id;
    String name;
    int marks;
    int total;

    public QuizMarksClass() {
    }

    public QuizMarksClass(int id, String name, int marks, int total) {
        this.id = id;
        this.name = name;
        this.marks = marks;
        this.total = total;
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

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
