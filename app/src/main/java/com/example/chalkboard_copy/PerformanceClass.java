package com.example.chalkboard_copy;

public class PerformanceClass {
    int id;
    String name;
    int performance;
    int marks;
    int total;
    int count;
    String quiz_name;
    double converted_quiz;
    double attendance;
    double thirtybackup;
    double cgpa;
    String grade;
    double final_marks;

    public PerformanceClass() {
    }

    public PerformanceClass(int id, String name, int performance, int marks, int total, int count, String quiz_name, double converted_quiz, double attendance, double thirtybackup, double cgpa, String grade, double final_marks) {
        this.id = id;
        this.name = name;
        this.performance = performance;
        this.marks = marks;
        this.total = total;
        this.count = count;
        this.quiz_name = quiz_name;
        this.converted_quiz = converted_quiz;
        this.attendance = attendance;
        this.thirtybackup = thirtybackup;
        this.cgpa = cgpa;
        this.grade = grade;
        this.final_marks = final_marks;
    }

    public double getThirtybackup() {
        return thirtybackup;
    }

    public void setThirtybackup(double thirtybackup) {
        this.thirtybackup = thirtybackup;
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

    public int getPerformance() {
        return performance;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public double getConverted_quiz() {
        return converted_quiz;
    }

    public void setConverted_quiz(double converted_quiz) {
        this.converted_quiz = converted_quiz;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getFinal_marks() {
        return final_marks;
    }

    public void setFinal_marks(double final_marks) {
        this.final_marks = final_marks;
    }
}
