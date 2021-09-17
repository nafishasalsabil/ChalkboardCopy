package com.example.chalkboard_copy;

public class InformationToAddNewCourse {
    String course_title,course_no,semester,credits,course_type,number_of_quizes;
    public InformationToAddNewCourse() {
    }

    public InformationToAddNewCourse(String course_title, String course_no, String semester, String credits, String course_type, String number_of_quizes) {
        this.course_title = course_title;
        this.course_no = course_no;
        this.semester = semester;
        this.credits = credits;
        this.course_type = course_type;
        this.number_of_quizes = number_of_quizes;
    }


    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public String getNumber_of_quizes() {
        return number_of_quizes;
    }

    public void setNumber_of_quizes(String number_of_quizes) {
        this.number_of_quizes = number_of_quizes;
    }
}

