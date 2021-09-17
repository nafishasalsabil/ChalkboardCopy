package com.example.chalkboard_copy;

public class NoticeClass {
    String title,description,name,posted_at,id,imageUrl;
    int like;

    public NoticeClass() {
    }

    public NoticeClass(String title, String description, String name, String posted_at, String id, String imageUrl, int like) {
        this.title = title;
        this.description = description;
        this.name = name;
        this.posted_at = posted_at;
        this.id = id;
        this.imageUrl = imageUrl;
        this.like = like;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
