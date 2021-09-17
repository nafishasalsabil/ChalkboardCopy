package com.example.chalkboard_copy;

public class NotesClass {
    String noteTitle,subtitle,date,mynote,url,search;

    public NotesClass() {
    }

    public NotesClass(String noteTitle, String subtitle, String date, String mynote, String url) {
        this.noteTitle = noteTitle;
        this.subtitle = subtitle;
        this.date = date;
        this.mynote = mynote;
        this.url = url;

    }


    public NotesClass(String noteTitle, String subtitle, String date, String mynote, String url, String search) {
        this.noteTitle = noteTitle;
        this.subtitle = subtitle;
        this.date = date;
        this.mynote = mynote;
        this.url = url;
        this.search = search;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMynote() {
        return mynote;
    }

    public void setMynote(String mynote) {
        this.mynote = mynote;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return url;
    }
}
