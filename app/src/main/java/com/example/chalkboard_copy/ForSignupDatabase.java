package com.example.chalkboard_copy;

public class ForSignupDatabase {
    String username, email, password,id,messages,active_status,search,imageUrl,choice;

    public ForSignupDatabase() {

    }


    public ForSignupDatabase(String username, String email, String password, String id, String messages, String active_status, String search, String imageUrl, String choice) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
        this.messages = messages;
        this.active_status = active_status;
        this.search = search;
        this.imageUrl = imageUrl;
        this.choice = choice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getActive_status() {
        return active_status;
    }

    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }


    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
