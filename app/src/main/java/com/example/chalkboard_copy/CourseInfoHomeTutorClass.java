package com.example.chalkboard_copy;

public class CourseInfoHomeTutorClass {
    String courseName,className;

    public CourseInfoHomeTutorClass() {
    }

    public CourseInfoHomeTutorClass(String courseName, String className) {
        this.courseName = courseName;
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
