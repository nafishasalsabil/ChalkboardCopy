package com.example.chalkboard_copy;

public class StatusClass {
    String name,status;
    int id;

    public StatusClass(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public StatusClass() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
