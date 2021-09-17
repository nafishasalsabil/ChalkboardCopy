package com.example.chalkboard_copy;

public class QuizNameClass {
    int id;
    String name;
    String quiz,quiz_date,quiz_marks;
    int quiz_total_marks;

    public QuizNameClass() {
    }

    public QuizNameClass(int id, String name, String quiz, String quiz_date, String quiz_marks, int quiz_total_marks) {
        this.id = id;
        this.name = name;
        this.quiz = quiz;
        this.quiz_date = quiz_date;
        this.quiz_marks = quiz_marks;
        this.quiz_total_marks = quiz_total_marks;
    }

    public QuizNameClass(String q_n, String q_d, int q) {
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getQuiz_date() {
        return quiz_date;
    }

    public void setQuiz_date(String quiz_date) {
        this.quiz_date = quiz_date;
    }

    public String getQuiz_marks() {
        return quiz_marks;
    }

    public void setQuiz_marks(String quiz_marks) {
        this.quiz_marks = quiz_marks;
    }

    public int getQuiz_total_marks() {
        return quiz_total_marks;
    }

    public void setQuiz_total_marks(int quiz_total_marks) {
        this.quiz_total_marks = quiz_total_marks;
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

    @Override
    public String toString() {
        return quiz;
    }
}
