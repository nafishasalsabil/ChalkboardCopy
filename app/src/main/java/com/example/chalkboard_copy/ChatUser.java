package com.example.chalkboard_copy;

public class ChatUser {
    String username,imageUrl ,id,email,choice,password,active_status,search;

    public ChatUser() {
    }

    public ChatUser(String username, String imageUrl, String id, String email, String choice, String password, String active_status, String search) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.id = id;
        this.email = email;
        this.choice = choice;
        this.password = password;
        this.active_status = active_status;
        this.search = search;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
