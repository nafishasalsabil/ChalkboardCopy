package com.example.chalkboard_copy;

public class TutorialsClass {
    String link_title,saved_link;

    public TutorialsClass() {
    }

    public TutorialsClass(String link_title, String saved_link) {
        this.link_title = link_title;
        this.saved_link = saved_link;
    }

    public String getLink_title() {
        return link_title;
    }

    public void setLink_title(String link_title) {
        this.link_title = link_title;
    }

    public String getSaved_link() {
        return saved_link;
    }

    public void setSaved_link(String saved_link) {
        this.saved_link = saved_link;
    }
}
