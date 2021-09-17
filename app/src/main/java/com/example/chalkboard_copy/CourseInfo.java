package com.example.chalkboard_copy;

public class CourseInfo {
    private String courseTitle,courseNo,semester,credits,courseType,noOfQuizes,section;

    public CourseInfo() {
    }

    public CourseInfo(String courseTitle, String courseNo, String semester, String credits, String courseType, String noOfQuizes,String section) {
        this.courseTitle = courseTitle;
        this.courseNo = courseNo;
        this.semester = semester;
        this.credits = credits;
        this.courseType = courseType;
        this.noOfQuizes = noOfQuizes;
        this.section = section;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getNoOfQuizes() {
        return noOfQuizes;
    }

    public void setNoOfQuizes(String noOfQuizes) {
        this.noOfQuizes = noOfQuizes;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return  this.getCourseTitle();
    }

}
